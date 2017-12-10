package binlight;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.*;
import org.fourthline.cling.binding.annotations.*;
import org.fourthline.cling.model.*;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.*;
import java.io.IOException;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.registry.RegistrationException;
/**
 *
 * @author Han
 */
public class Phone implements Runnable{    
    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread serverThread = new Thread(new Phone());
        serverThread.setDaemon(false);
        serverThread.start();
    }
    private LocalDevice device;
    @Override
    public void run() {
        try {
            final UpnpService upnpService = new UpnpServiceImpl();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    upnpService.shutdown();
                }
            });
            device=createDevice();
            getStatus();
            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(
                    device
            );            
        } catch (IOException | LocalServiceBindingException | ValidationException | RegistrationException ex) {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    public void setStatus(boolean newStatus){
        LocalService chouSeiService=device.findService(new ServiceId("upnp-org", "Renraku"));        
        if(chouSeiService!=null){                
            Action action=chouSeiService.getAction("SetStatus");
            if(action!=null){
                SetActionInvocation actionInvocation = new SetActionInvocation(action,"NewStatusValue",newStatus);
                (new ActionCallback(actionInvocation) {
                    @Override
                    public void success(ActionInvocation invocation) {
//                        System.out.println("SetStatus: OK");
                    }                    
                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.out.println(defaultMsg);
                    }
                }).run();
            }                
        }
    }
    public boolean getStatus(){
        boolean result=false;
        LocalService renRakuService=device.findService(new ServiceId("upnp-org", "Renraku"));        
        if(renRakuService!=null){                
            Action action=renRakuService.getAction("GetStatus");
            if(action!=null){
                GetActionInvocation actionInvocation = new GetActionInvocation(action);
                (new ActionCallback(actionInvocation) {
                    @Override
                    public void success(ActionInvocation invocation) {                                                       
//                        System.out.println("GetStatus: OK");                       
                    }
                    
                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.out.println(defaultMsg);
                    }
                }).run();
                result=(boolean)(actionInvocation.getOutput("ResultStatusValue").getValue());
            }                
        }
        return result;        
    }
    private LocalDevice createDevice()
        throws ValidationException, LocalServiceBindingException, IOException {
        DeviceIdentity identity =
                new DeviceIdentity(
                        UDN.uniqueSystemIdentifier("Demo Phone")
                );
        DeviceType type =
                new UDADeviceType("Phone", 1);
        DeviceDetails details =
                new DeviceDetails(
                        "Nokia Phone",
                        new ManufacturerDetails("NOKIA"),
                        new ModelDetails(
                                "Nokia1110i",
                                "A demo call-hear only phone",
                                "v1"
                        )
                );
        Icon icon =
                new Icon(
                        "image/png", 48, 48, 8,
                        getClass().getResource("phone.png")
                );
        LocalService<Renraku> myService =
                new AnnotationLocalServiceBinder().read(Renraku.class);
        myService.setManager(
                new DefaultServiceManager(myService, Renraku.class)
        );
        return new LocalDevice(identity, type, details, icon, myService);
        /* Several services can be bound to the same device:
        return new LocalDevice(
                identity, type, details, icon,
                new LocalService[] {myService, myOtherService}
        );
        */

    }
}
