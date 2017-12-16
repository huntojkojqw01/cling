package smartHome;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import static org.fourthline.cling.binding.xml.Descriptor.Device.ELEMENT.service;
import org.fourthline.cling.controlpoint.*;
import org.fourthline.cling.model.action.*;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.*;
import org.fourthline.cling.model.message.header.*;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.*;
import org.fourthline.cling.registry.*;
/**
 *
 * @author Han
 */
public class ControlPoint extends ControlPointGUI implements Runnable{
    private final javax.swing.JTextArea cpTextArea;
    private final javax.swing.JComboBox deviceBox;
    private final UpnpService upnpService;
    private final int abc;
    public ControlPoint(){
        upnpService = new UpnpServiceImpl();
        cpTextArea = this.getCPArea();
        deviceBox = this.getDeviceBox();
        
            System.out.println(deviceBox.getModel().getElementAt(2));
            System.out.println(deviceBox.getModel().getSelectedItem());
            
            
       

        abc=5;
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
//            
//deviceBox.setModel(new DefaultComboBoxModel());
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
                Service service;               
                if ((service = device.findService(serviceId)) != null) {
                    System.out.println("Device is: "+ device);
                    System.out.println("Service discovered: " + service);
//                    executeAction(upnpService, service, "SetVolume", "NewVolumeValue", 33);
                    executeAction(upnpService, service, "SetStatus", "NewStatusValue", true);
                    cpTextArea.append(service.getServiceId().toString()+"\n");
                    SubscriptionCallback callback = new SubscriptionCallback(service, 600) { // Timeout in seconds

                        @Override
                        public void established(GENASubscription sub) {
                            System.out.println("Established: " + sub.getSubscriptionId());
                        }

                        @Override
                        public void failed(GENASubscription sub, UpnpResponse response, Exception ex) {
                            System.err.println(
                                createDefaultFailureMessage(response, ex)
                            );
                        }

                        @Override
                        public void ended(GENASubscription sub, CancelReason reason, UpnpResponse response) {
                            // Reason should be null, or it didn't end regularly
                        }

                        @Override
                        public void eventReceived(GENASubscription sub) {
//                            System.out.println(abc);
//                            System.out.println("Event: " + sub.getCurrentSequence().getValue());
                            Map<String, StateVariableValue> values = sub.getCurrentValues();
//                            System.out.println(sub);
                            StateVariableValue status = values.get("Status");
                            
                            
                            if(status!=null) System.out.println("Status is: " + status.toString());
                            else{
                                System.out.println("Ko co status.");
                            }
                        }

                        @Override
                        public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                            System.out.println("Missed events: " + numberOfMissedEvents);
                        }

                        @Override
                        protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                   };
                   upnpService.getControlPoint().execute(callback);
                }
            }
            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                Service service;
                if ((service = device.findService(serviceId)) != null) {
                    System.out.println("Device is: "+ device);
                    System.out.println("Service disappeared: " + service);                    
                    cpTextArea.setText(cpTextArea.getText().replace(service.getServiceId().toString()+"\n", ""));
                }
            }
            private void executeAction(
                    UpnpService upnpService,
                    Service service,
                    String actionName,
                    String agrumentName,
                    Object value
            ) {
                // Begin check params xem co hop le hay khong, hop le thi moi execute action:
                Action action = service.getAction(actionName);
                boolean paramsOk = false;
                if(action != null){
                    ActionArgument argument = action.getInputArgument(agrumentName);
                    if(argument != null){                        
                        if(argument.getDatatype().isHandlingJavaType(value.getClass())){
                            System.out.println("OK ALL");
                            paramsOk = true;
                        }
                        else System.out.println("ValueType is invalid.");
                    }
                    else System.out.println("ArgumentName is invalid.");                    
                }
                else System.out.println("ActionName is invalid.");
                // end Check params.
                
                if(paramsOk){
                    ActionInvocation invocation =
                    new CallAction(service,actionName,agrumentName,value);
                    // Executes asynchronous in the background
                    upnpService.getControlPoint().execute(
                        new ActionCallback(invocation) {
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
                }else System.out.println("=>Params is invalid");
            }
        };
    }
    public Object[] getDevices(){
        return upnpService.getRegistry().getDevices().toArray();
    }
}
class CallAction extends ActionInvocation {
    public CallAction(Service service,String actionName,String agrumentName,Object value) {
        super(service.getAction(actionName));        
        try {
            // Throws InvalidValueException if the value is of wrong type
            setInput(agrumentName,value);          
            System.out.println("Called: "+actionName+">"+agrumentName+">"+String.valueOf(value));
        } catch (InvalidValueException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}