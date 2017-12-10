package binlight;
import java.awt.Color;
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
public class Renraku extends PhoneGUI{
    private final PropertyChangeSupport propertyChangeSupport;
    private final javax.swing.JButton callButton;
    public Renraku() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);    
        callButton=this.getCallButton();
        callButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CallButtonActionPerformed(evt);
            }
        });
    }
    private void CallButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:        
        setStatus(!status);        
    } 
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }    
    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;
    @UpnpAction
    public void setStatus(@UpnpInputArgument(name = "NewStatusValue")
                          boolean newTargetValue) {
        status = newTargetValue;        
        getPropertyChangeSupport().firePropertyChange("Status", null, status);
        
        //GIAO DIEN THAY DOI O DAY:
        callButton.setText(status==true ? "CALLING" : "NO CALL");
        callButton.setBackground(status==true ? Color.red : Color.green);
    }
    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatusValue"))
    public boolean getStatus() {
        // If you want to pass extra UPnP information on error:
        // throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);        
        return status;
    }
}
