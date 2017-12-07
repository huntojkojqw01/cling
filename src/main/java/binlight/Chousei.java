package binlight;
import org.fourthline.cling.binding.annotations.*;
import java.beans.PropertyChangeSupport;
/**
 *
 * @author Han
 */
@UpnpService(
        serviceId = @UpnpServiceId("Chousei"),
        serviceType = @UpnpServiceType(value = "Chousei", version = 1)
)
public class Chousei {
    private final PropertyChangeSupport propertyChangeSupport;
    public Chousei() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private boolean target = false;
    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;
    @UpnpStateVariable(defaultValue = "0")
    private int volume = 0;
    @UpnpAction
    public void setTarget(@UpnpInputArgument(name = "NewTargetValue")
                          boolean newTargetValue) {
        target = newTargetValue;
        status = newTargetValue;
        System.out.println("Switch is: " + status);
        getPropertyChangeSupport().firePropertyChange("Status", null, status);
    }
    @UpnpAction
    public void setVolume(@UpnpInputArgument(name = "NewVolumeValue")
                          int newVolumeValue) {
        volume = newVolumeValue;        
        System.out.println("Current volume is: " + volume);
        getPropertyChangeSupport().firePropertyChange("Volume", null, volume);
    }
    @UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
    public boolean getTarget() {
        return target;
    }
    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public boolean getStatus() {
        // If you want to pass extra UPnP information on error:
        // throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);
        return status;
    }
    @UpnpAction(out = @UpnpOutputArgument(name = "RetVolumeValue"))
    public int getVolume() {
        return volume;
    }
}
