package smartHome;
import static java.awt.Frame.SE_RESIZE_CURSOR;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.*;
import org.fourthline.cling.model.ServiceReference;
import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.action.*;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.gena.RemoteGENASubscription;
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
    private final javax.swing.JTextArea infoArea;    
    private final javax.swing.JComboBox deviceBox;
    private final javax.swing.JComboBox serviceBox;
    private final javax.swing.JComboBox actionBox;
    private final javax.swing.JComboBox argumentBox;    
    private final javax.swing.JCheckBox valueCheckBox;
    private final javax.swing.JSpinner valueSpinner;
    private final javax.swing.JButton callActionButton;
    private final javax.swing.JTextArea resultArea;
    private final UpnpService upnpService;
    private RemoteDevice gdevice=null;
    private Service gservice=null;
    private Action gaction=null;
    private ActionArgument gargument=null;
    
    public ControlPoint(){
        upnpService = new UpnpServiceImpl();
        infoArea = this.getInfoArea();
        deviceBox = this.getDeviceBox();
        serviceBox = this.getServiceBox();        
        actionBox = this.getActionBox();
        argumentBox = this.getArgumentBox();
        valueCheckBox = this.getCheckBox();
        valueSpinner = this.getSpinner();
        callActionButton = this.getCallActionButton();
        resultArea = this.getResultArea();
        deviceBox.setVisible(false);
        serviceBox.setVisible(false);       
        actionBox.setVisible(false);
        argumentBox.setVisible(false);
        valueCheckBox.setVisible(false);
        valueSpinner.setVisible(false);
        
        deviceBox.addActionListener(new java.awt.event.ActionListener() {
             @Override
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 DeviceBoxActionPerformed(evt);
             }
        });

        serviceBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServiceBoxActionPerformed(evt);
            }
        });

        actionBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActionBoxActionPerformed(evt);
            }
        });
        argumentBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArgumentBoxActionPerformed(evt);
            }
        });
        deviceBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                DeviceBoxPropertyChange(evt);
            }
        });

        serviceBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ServiceBoxPropertyChange(evt);
            }
        });

        actionBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ActionBoxPropertyChange(evt);
            }
        });
        
        argumentBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ArgumentBoxPropertyChange(evt);
            }
        });
        
        callActionButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CallActionButtonActionPerformed(evt);
            }
        });
    }
    
    private void DeviceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
        if(deviceBox.getSelectedItem()!=null){            
            deviceBox.setVisible(true);        
            gdevice = upnpService.getRegistry().getRemoteDevice((UDN) deviceBox.getSelectedItem(), rootPaneCheckingEnabled);
    //        System.out.println(remoteDevice);
            infoArea.setText(gdevice.getDetails().getFriendlyName());
            infoArea.append(getInfoDevice(gdevice,null,null));
            serviceBox.removeAllItems();
            for (RemoteService service1 : gdevice.getServices()) {
    //            System.out.println(service1);
                serviceBox.addItem(service1.getServiceId().getId());
            }        
        }
        else{
            deviceBox.setVisible(false);
        }
    }
    
    private void ServiceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        actionBox.removeAllItems();
        if(serviceBox.getSelectedItem()!=null){
            serviceBox.setVisible(true);      
            gservice = gdevice.findService(new UDAServiceId(serviceBox.getSelectedItem().toString()));
//            System.out.println(gservice);
            
            if(gservice!=null){                
                for (Action action : gservice.getActions()) {
                    actionBox.addItem(action.getName());
                }
            }
        }
        else{
            serviceBox.setVisible(false);
        }
    }                                          

    private void ActionBoxActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
        argumentBox.removeAllItems();
        if(actionBox.getSelectedItem()!=null){
            actionBox.setVisible(true);
            gaction = gservice.getAction(actionBox.getSelectedItem().toString());            
            
            if(gaction!=null){                
                for (ActionArgument argument : gaction.getInputArguments()) {
                    argumentBox.addItem(argument.getName());
                }
            }
        }
        else{
            actionBox.setVisible(false);
        }
    }                                         

    private void ArgumentBoxActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
        
        if(argumentBox.getSelectedItem()!=null){
            argumentBox.setVisible(true);            
            gargument = gaction.getInputArgument(argumentBox.getSelectedItem().toString());            
            if(gargument!=null){                
                if(gargument.getDatatype().isHandlingJavaType(Integer.class)){
                                   
                    valueSpinner.setVisible(true);
                    valueCheckBox.setVisible(false);
                }
                else{
                    
                    valueSpinner.setVisible(false);
                    valueCheckBox.setVisible(true);                  
                    
                }
            }
        }
        else{
            argumentBox.setVisible(false);
            valueSpinner.setVisible(false);
            valueCheckBox.setVisible(false);
        }
    }
    
    
    private void CallActionButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
        String actionName = null;
        String argumentName = null;
        Object value = null;
        
        if(gaction!=null)actionName=gaction.getName();
        
        if(gargument!=null){
            argumentName = gargument.getName();
            if(gargument.getDatatype().isHandlingJavaType(Boolean.class)) value = valueCheckBox.isSelected();
            else value = valueSpinner.getValue();
        }
        
        System.out.println("Params:\n    "+upnpService+"\n    "+gservice+"\n    "+actionName+"\n    "+argumentName+"\n    "+value);
        executeAction(
                    upnpService,
                    gservice,
                    actionName,
                    argumentName,
                    value
            );                    
    }
    
    private void DeviceBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                         
        // TODO add your handling code here:
        serviceBox.setVisible(deviceBox.isVisible());        
    }                                        

    private void ServiceBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                          
        // TODO add your handling code here:
        actionBox.setVisible(serviceBox.isVisible());
    }                                         

    private void ActionBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                         
        // TODO add your handling code here:
        argumentBox.setVisible(actionBox.isVisible());
    }                                        

    private void ArgumentBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                           
        // TODO add your handling code here:       
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
                    createRegistryListener(upnpService)
            );            
            // Broadcast a search message for all devices
            upnpService.getControlPoint().search(
                    new STAllHeader()
            );           

        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            System.exit(1);
        }        
    }
    private RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {
//            ServiceId serviceId = new UDAServiceId(serviceID);
            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
//                Service service;
                System.out.println("Added : "+ device);                
                deviceBox.addItem(device.getIdentity().getUdn());    
                
                for (RemoteService service : device.getServices()) {
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
//                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }

                        @Override
                        public void eventReceived(GENASubscription sub) {//

                            System.out.println("Event number "+sub.getCurrentSequence().getValue()+" from "+sub.getSubscriptionId());
                            Map<String, StateVariableValue> values = sub.getCurrentValues();
                            
                            for (Map.Entry<String, StateVariableValue> entry : values.entrySet()) {
                                String key = entry.getKey();
                                StateVariableValue value = entry.getValue();
                                System.out.println("   "+key+" is "+value);
                                
                            }
                            StateVariableValue status = values.get("Status");                            
                            
                            if(status != null){
                                RemoteDevice tmpDevice = null;
                                Service tmpService = null;
                                for (RemoteDevice remoteDevice : upnpService.getRegistry().getRemoteDevices()) {
                                    tmpService = remoteDevice.findService(new UDAServiceType("Chousei"));
                                    if(tmpService != null){
//                                        System.out.println("tim thay "+tmpService);
                                            System.out.println(status.getValue());
                                        executeAction(upnpService, tmpService, "SetPower", "NewPowerValue", status.getValue());
                                    }        
                                }
                            }         
                        }

                        @Override
                        public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                            System.out.println("Missed events: " + numberOfMissedEvents);
                        }                                         

                        @Override
                        protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
//                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                        
                   };
                   upnpService.getControlPoint().execute(callback);
                }
            }
            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                deviceBox.removeItem(device.getIdentity().getUdn());
                System.out.println("Remove : "+ device);
            }          
        };
    }
    
    public Collection<RemoteDevice> getDevices(){            
        return upnpService.getRegistry().getRemoteDevices();
    }
    
    public String currentTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    }

    private String getInfoService(RemoteService service,String stateVariable,Object value){
        String string = "";
        if(service!=null){
            for (StateVariable<RemoteService> stateVariable1 : service.getStateVariables()) {
                if(stateVariable!=null &&  stateVariable.equals(stateVariable1.getName()))
                    string += "\n       |___"+stateVariable1.getName()+": "+value;
                else
                    string += "\n       |___"+stateVariable1.getName();
            }
            return string;
        }
        else return null;
    }
    
    private String getInfoDevice(RemoteDevice device,String stateVariable,Object value){
        String string = "";
        if(device!=null){
            for (RemoteService service : device.getServices()) {
                string += "\n|___"+service.getServiceId().getId();
                string += getInfoService(service,stateVariable,value);
            }
            return string;
        }
        else return null;
    }
    
    private void executeAction(
                    UpnpService upnpService, Service service,
                    String actionName, String agrumentName, Object value
            ) {
                // Begin check params xem co hop le hay khong, hop le thi moi execute action:
                Action action = service.getAction(actionName);
                boolean paramsOk = false;
                if(action != null){
//                    action.getInputArgument(actionName).getDirection().compareTo(ActionArgument.Direction.IN);
                    if(action.hasInputArguments()){                                          
                        ActionArgument argument = action.getInputArgument(agrumentName);
                        if(argument != null){                        
                            if(argument.getDatatype().isHandlingJavaType(value.getClass())){
//                                System.out.println("Params OK");
                                paramsOk = true;
                            }
                            else System.out.println("ValueType is invalid.");
                        }
                        else System.out.println("ArgumentName is invalid.");
                    }
                    else{
                        agrumentName = null;
                        value = null;
//                        System.out.println("Not need argument, Params OK");
                        paramsOk = true;
                    }
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
//                                System.out.println("Successfully called action!");
                                
                                for (ActionArgumentValue actionArgumentValue : invocation.getOutput()) {                                    
                                    resultArea.append("\nResult( "+currentTime()+" ):\n   "
                                            +actionArgumentValue.getArgument().getName()+": "
                                            +actionArgumentValue);
                                    resultArea.setCaretPosition(resultArea.getText().length());
                                }
                            }
                            @Override
                            public void failure(ActionInvocation invocation,UpnpResponse operation,String defaultMsg) {
                                System.err.println(defaultMsg);
                            }
                        }
                    );
                }else System.out.println("=>Params is invalid");
            }           
}
class CallAction extends ActionInvocation {
    public CallAction(Service service,String actionName,String agrumentName,Object value) {
        super(service.getAction(actionName));        
        try {
            // Throws InvalidValueException if the value is of wrong type
            if(agrumentName!=null && value!=null) setInput(agrumentName,value);          
            System.out.println("Called: "+actionName+">"+agrumentName+">"+String.valueOf(value));
        } catch (InvalidValueException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}