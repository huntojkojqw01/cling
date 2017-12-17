package smartHome;
import java.awt.Color;
import java.awt.Image;
import org.fourthline.cling.binding.annotations.*;
import java.beans.PropertyChangeSupport;
import javax.swing.ImageIcon;
/**
 *
 * @author Han
 */
@UpnpService(
        serviceId = @UpnpServiceId("Chousei"),
        serviceType = @UpnpServiceType(value = "Chousei", version = 1)
)
public class Chousei extends SpeakerGUI{
    private final PropertyChangeSupport propertyChangeSupport;
    private final javax.swing.JToggleButton powerButton;
    private final javax.swing.JSlider volumeSlider;
    private final javax.swing.JLabel volumeLabel;
    public Chousei(){        
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        powerButton=this.getPowerButton();
        powerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PowerButtonActionPerformed(evt);
            }
        });
        volumeSlider=this.getVolumeSlider();
        volumeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                VolumeSliderStateChanged(evt);
            }
        });
        volumeLabel=this.getVolumeLabel();
    }
    private void PowerButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here: 
        setPower(powerButton.isSelected());
    }
    private void VolumeSliderStateChanged(javax.swing.event.ChangeEvent evt) {                                          
        // TODO add your handling code here:        
        if(!volumeSlider.getValueIsAdjusting())
            setVolume((int)volumeSlider.getValue()); 
        else
            volumeLabel.setText(String.valueOf(volumeSlider.getValue()));
    }  
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
    @UpnpStateVariable(defaultValue = "0")
    private boolean power = false;    
    @UpnpStateVariable(defaultValue = "0")
    private int volume = 0;
    @UpnpAction// đây là hàm set bật tắt cái Speaker:
    public void setPower(@UpnpInputArgument(name = "NewPowerValue")
                          boolean newPowerValue) {
        power = newPowerValue;
        powerButton.setSelected(power);
        ImageIcon icon;
        if(power==true){            
            icon = new ImageIcon("src/main/resources/on-switch.png");
        }            
        else{
            icon = new ImageIcon("src/main/resources/off-switch.png");
        }
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance( powerButton.getWidth(), powerButton.getHeight(),  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        powerButton.setIcon(icon);
        
        getPropertyChangeSupport().firePropertyChange("Power", null, power);
    }    
    @UpnpAction 
    public void setVolume(@UpnpInputArgument(name = "NewVolumeValue")
                          int newVolumeValue) {
        if(newVolumeValue<0)        volume=0;
        else if(newVolumeValue>100) volume=100;
        else                        volume = newVolumeValue;        
        volumeLabel.setText(String.valueOf(volume));
        volumeSlider.setValue(volume);
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
