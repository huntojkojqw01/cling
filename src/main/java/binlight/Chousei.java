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
    @UpnpStateVariable(defaultValue = "0")
    private boolean power = false;    
    @UpnpStateVariable(defaultValue = "0")
    private int volume = 0;
    @UpnpAction
    public void setPower(@UpnpInputArgument(name = "NewPowerValue")
                          boolean newPowerValue) {
        power = newPowerValue;
        if(power==true)
            System.out.println("Speaker is ON.");
        else
            System.out.println("Speaker is OFF.");        
        getPropertyChangeSupport().firePropertyChange("Power", null, power);
    }    
    @UpnpAction
    public void setVolume(@UpnpInputArgument(name = "NewVolumeValue")
                          int newVolumeValue) {
        volume = newVolumeValue;        
        System.out.println("Current volume is: " + volume);
        getPropertyChangeSupport().firePropertyChange("Volume", null, volume);
    }
    @UpnpAction(out = @UpnpOutputArgument(name = "ResultPowerValue"))
    public boolean getPower() {
        // If you want to pass extra UPnP information on error:
        // throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);
        return power;
    }
    @UpnpAction(out = @UpnpOutputArgument(name = "ResultVolumeValue"))    
    public int getVolume() {
        return volume;
    }    
}
