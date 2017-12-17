package smartHome;
import java.awt.Color;
import java.awt.event.ActionEvent;
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
    private final javax.swing.JButton cancelButton;
    private final javax.swing.JButton btn;
    public Renraku() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);    
        callButton=this.getCallButton();
        btn=this.getbtn();
        cancelButton=this.getCancelButton();
        callButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CallButtonActionPerformed(evt);
            }
        });
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });
    }
    private void CallButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:        
        setStatus(true);        
    }
    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
        setStatus(false);
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
        
        if(status == true){
            btn.setText("IS CALLING");
        }
        else if(status == false){
            btn.setText("NOT CALLING");
        }
    }
    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatusValue"))
    public boolean getStatus() {
        // If you want to pass extra UPnP information on error:
        // throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);        
        return status;
    }
}
