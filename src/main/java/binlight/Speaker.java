package binlight;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.*;
import org.fourthline.cling.binding.annotations.*;
import org.fourthline.cling.model.*;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.*;
import java.io.IOException;
import org.fourthline.cling.registry.RegistrationException;
/**
 *
 * @author Han
 */
public class Speaker implements Runnable {
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
            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(
                    createDevice()
            );         
        } catch (IOException | LocalServiceBindingException | ValidationException | RegistrationException ex) {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
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
        LocalService<SwitchPower> switchPowerService =
                new AnnotationLocalServiceBinder().read(SwitchPower.class);
        switchPowerService.setManager(
                new DefaultServiceManager(switchPowerService, SwitchPower.class)
        );
        return new LocalDevice(identity, type, details, icon, switchPowerService);
        /* Several services can be bound to the same device:
        return new LocalDevice(
                identity, type, details, icon,
                new LocalService[] {switchPowerService, myOtherService}
        );
        */
    }
}
