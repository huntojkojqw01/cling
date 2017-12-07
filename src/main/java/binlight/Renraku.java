package binlight;
import org.fourthline.cling.binding.annotations.*;
import java.beans.PropertyChangeSupport;
/**
 *
 * @author Han
 */
@UpnpService(
        serviceId = @UpnpServiceId("Renraku"),
        serviceType = @UpnpServiceType(value = "Renraku", version = 1)
)
public class Renraku {
    private final PropertyChangeSupport propertyChangeSupport;
    public Renraku() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private boolean target = false;
    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;
    @UpnpAction
    public void setTarget(@UpnpInputArgument(name = "NewTargetValue")
                          boolean newTargetValue) {
        target = newTargetValue;
        status = newTargetValue;
        if(status==true){
            System.out.println("Have a call");
        }
        else{
            System.out.println("No call.");
        }
        getPropertyChangeSupport().firePropertyChange("Status", null, status);
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
}
