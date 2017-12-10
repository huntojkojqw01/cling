package smartHome;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.*;
import org.fourthline.cling.model.action.*;
import org.fourthline.cling.model.message.*;
import org.fourthline.cling.model.message.header.*;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.*;
import org.fourthline.cling.registry.*;
/**
 *
 * @author Han
 */
public class ControlPoint extends ControlPointGUI implements Runnable{
    private final javax.swing.JTextArea cpTextArea;
    public ControlPoint(){
        cpTextArea=this.getCPArea();
    }
    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread clientThread = new Thread(new ControlPoint());
        clientThread.setDaemon(false);
        clientThread.start();
    }
    @Override
    public void run() {
        try {
            UpnpService upnpService = new UpnpServiceImpl();
            // Add a listener for device registration events
            upnpService.getRegistry().addListener(
                    createRegistryListener(upnpService,"Chousei")
            );
            upnpService.getRegistry().addListener(
                    createRegistryListener(upnpService,"Renraku")
            );
            // Broadcast a search message for all devices
            upnpService.getControlPoint().search(
                    new STAllHeader()
            );            
        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            System.exit(1);
        }        
    }
    private RegistryListener createRegistryListener(final UpnpService upnpService,final String serviceID) {
        return new DefaultRegistryListener() {
            ServiceId serviceId = new UDAServiceId(serviceID);
            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                Service chouseiService;
                if ((chouseiService = device.findService(serviceId)) != null) {
                    System.out.println("Device is: "+ device);
                    System.out.println("Service discovered: " + chouseiService);
                    cpTextArea.append(chouseiService.getServiceId().toString()+"\n");                    
                }
            }
            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                Service chouseiService;
                if ((chouseiService = device.findService(serviceId)) != null) {
                    System.out.println("Device is: "+ device);
                    System.out.println("Service disappeared: " + chouseiService);                    
                    cpTextArea.setText(cpTextArea.getText().replace(chouseiService.getServiceId().toString()+"\n", ""));
                }
            }
            private void executeAction(UpnpService upnpService, Service service) {
                ActionInvocation setTargetInvocation =
                    new SetTargetActionInvocation(service);
                // Executes asynchronous in the background
                upnpService.getControlPoint().execute(
                    new ActionCallback(setTargetInvocation) {
                        @Override
                        public void success(ActionInvocation invocation) {                            
                            System.out.println("Successfully called action!");
                        }
                        @Override
                        public void failure(ActionInvocation invocation,UpnpResponse operation,String defaultMsg) {
                            System.err.println(defaultMsg);
                        }
                    }
                );
            }
        };
    }
}
class SetTargetActionInvocation extends ActionInvocation {
    public SetTargetActionInvocation(Service service) {
        super(service.getAction("SetVolume"));
        try {
            // Throws InvalidValueException if the value is of wrong type
            setInput("NewVolumeValue", 12);
            System.out.println("CP goi action thanh cong");
        } catch (InvalidValueException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}