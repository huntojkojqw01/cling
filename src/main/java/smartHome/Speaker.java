package smartHome;
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
public class Speaker implements Runnable {    
    private LocalDevice device;
    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread serverThread = new Thread(new Speaker());
        serverThread.setDaemon(false);
        serverThread.start();        
    }    
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
            setPower(false);
            setVolume(0);
            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(device);            
        } catch (IOException | LocalServiceBindingException | ValidationException | RegistrationException ex) {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    public int getVolume(){
        int result=0;
        LocalService chouSeiService=device.findService(new ServiceId("upnp-org", "Chousei"));        
        if(chouSeiService!=null){                
            Action action=chouSeiService.getAction("GetVolume");
            if(action!=null){
                GetActionInvocation actionInvocation = new GetActionInvocation(action);
                (new ActionCallback(actionInvocation) {
                    @Override
                    public void success(ActionInvocation invocation) {                                                       
//                        System.out.println("GetVolume: OK");                      
                    }
                    
                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.out.println(defaultMsg);
                    }
                }).run();
                result=(int)(actionInvocation.getOutput("ResultVolumeValue").getValue());
            }                
        }
        return result;        
    }
    public void setVolume(int newVolume){
        LocalService chouSeiService=device.findService(new ServiceId("upnp-org", "Chousei"));        
        if(chouSeiService!=null){                
            Action action=chouSeiService.getAction("SetVolume");
            if(action!=null){
                SetActionInvocation actionInvocation = new SetActionInvocation(action,"NewVolumeValue",newVolume);
                (new ActionCallback(actionInvocation) {
                    @Override
                    public void success(ActionInvocation invocation) {
//                        System.out.println("SetVolume: OK");
                    }
                    
                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.out.println(defaultMsg);
                    }
                }).run();
            }                
        }
    }
    public boolean getPower(){
        boolean result=false;
        LocalService chouSeiService=device.findService(new ServiceId("upnp-org", "Chousei"));        
        if(chouSeiService!=null){                
            Action action=chouSeiService.getAction("GetPower");
            if(action!=null){
                GetActionInvocation actionInvocation = new GetActionInvocation(action);
                (new ActionCallback(actionInvocation) {
                    @Override
                    public void success(ActionInvocation invocation) {                                                       
//                        System.out.println("GetPower: OK");                       
                    }
                    
                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.out.println(defaultMsg);
                    }
                }).run();
                result=(boolean)(actionInvocation.getOutput("ResultPowerValue").getValue());
            }                
        }
        return result;        
    }
    public void setPower(boolean newPower){
        LocalService chouSeiService=device.findService(new ServiceId("upnp-org", "Chousei"));        
        if(chouSeiService!=null){                
            Action action=chouSeiService.getAction("SetPower");
            if(action!=null){
                SetActionInvocation actionInvocation = new SetActionInvocation(action,"NewPowerValue",newPower);
                (new ActionCallback(actionInvocation) {
                    @Override
                    public void success(ActionInvocation invocation) {
                        System.out.println("SetPower: OK");
                    }
                    
                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.out.println(defaultMsg);
                    }
                }).run();
            }                
        }
    }
    private LocalDevice createDevice()
        throws ValidationException, LocalServiceBindingException, IOException {
        DeviceIdentity identity =
                new DeviceIdentity(
                        UDN.uniqueSystemIdentifier("Demo Speaker")
                );
        DeviceType type =
                new UDADeviceType("SpeakerType", 1);
        DeviceDetails details =
                new DeviceDetails(
                        "Speaker By Hero",
                        new ManufacturerDetails("HERO"),
                        new ModelDetails(
                                "Speaker2017",
                                "A demo speaker with on/off volume.",
                                "v1"
                        )
                );
        Icon icon =
                new Icon(
                        "image/jpg", 48, 48, 8,
                        getClass().getResource("speaker.jpg")
                );
        LocalService<Chousei> myService =
                new AnnotationLocalServiceBinder().read(Chousei.class);        
        myService.setManager(
                new DefaultServiceManager(myService, Chousei.class)
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