package smartHome;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.*;
import org.fourthline.cling.model.action.*;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.*;
import org.fourthline.cling.model.message.header.*;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.*;
import org.fourthline.cling.model.types.BooleanDatatype;
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
    private final javax.swing.JTextField valueTextField;
    private final javax.swing.JButton callActionButton;
    private final javax.swing.JTextArea resultArea;
    private final javax.swing.JLabel deviceLabel,serviceLabel,actionLabel,argumentLabel;
    //==============================================
    private javax.swing.JCheckBox dCheckBox;
    private javax.swing.JComboBox<String> dDeviceBox;
    private javax.swing.JComboBox<String> dServiceBox;
    private javax.swing.JSpinner dSpinner;
    private javax.swing.JComboBox<String> dVariableBox;
    private javax.swing.JTextField dTextField;
       
    private javax.swing.JCheckBox sCheckBox;
    private javax.swing.JComboBox<String> sDeviceBox;
    private javax.swing.JComboBox<String> sServiceBox;
    private javax.swing.JSpinner sSpinner;
    private javax.swing.JComboBox<String> sVariableBox;
    private javax.swing.JTextField sTextField;
    
    private javax.swing.JButton setButton;
    private javax.swing.JButton unSetButton;
    private javax.swing.JTextArea listArea;
    //=================================================
    private final UpnpService upnpService;
    private RemoteDevice gdevice=null;
    private Service gservice=null;
    private Action gaction=null;
    private ActionArgument gargument=null;
    private final Map<RemoteDevice, HashMap> deviceMap;
    private final List<Map<String, Object>> listMap;
    
    private Collection<RemoteDevice> gdevices=null,gdevices2=null;
    private Collection<Service> gservices=null,gservices2=null;
    private Collection<StateVariable> gvariables=null,gvariables2=null;
    
    public ControlPoint(){
        deviceMap =new HashMap<RemoteDevice, HashMap>();
        listMap = new ArrayList<Map<String, Object>>();        
        upnpService = new UpnpServiceImpl();
        infoArea = this.getInfoArea();
        deviceBox = this.getDeviceBox();
        serviceBox = this.getServiceBox();        
        actionBox = this.getActionBox();
        argumentBox = this.getArgumentBox();
        valueCheckBox = this.getCheckBox();
        valueSpinner = this.getSpinner();
        valueTextField = this.getTextField();
        callActionButton = this.getCallActionButton();
        resultArea = this.getResultArea();
        deviceLabel= this.getDeviceLabel();
        serviceLabel=this.getServiceLabel();
        actionLabel=this.getActionLabel();
        argumentLabel=this.getArgumentLabel();
        //====================================
//        deviceBox.setVisible(false);
//        serviceBox.setVisible(false);       
//        actionBox.setVisible(false);
//        argumentBox.setVisible(false);
//        valueCheckBox.setVisible(false);
//        valueSpinner.setVisible(false);
//        valueTextField.setVisible(false);
//        //=============================
        dDeviceBox=this.getDDeviceBox();
        dServiceBox=this.getDServiceBox();
        dVariableBox=this.getDVariableBox();
        dCheckBox=this.getDCheckBox();
        dSpinner=this.getDSpinner();
        dTextField=this.getDTextField();
        sDeviceBox=this.getSDeviceBox();
        sServiceBox=this.getSServiceBox();
        sVariableBox=this.getSVariableBox();
        sCheckBox=this.getSCheckBox();
        sSpinner=this.getSSpinner();
        sTextField=this.getSTextField();
        
        setButton=this.getSetButton();
        unSetButton=this.getUnSetButton();
        listArea=this.getListArea();
        
//        dDeviceBox.setVisible(false);               
//        dServiceBox.setVisible(false);
//        sDeviceBox.setVisible(false);
//        sServiceBox.setVisible(false);
//        dVariableBox.setVisible(false);
//        sVariableBox.setVisible(false);
//        dCheckBox.setVisible(false);
//        sCheckBox.setVisible(false);
//        dSpinner.setVisible(false);
//        sSpinner.setVisible(false);
        
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
        
        //----------------------------------------------------------------------
        sDeviceBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SDeviceBoxActionPerformed(evt);
            }
        });
        sDeviceBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                SDeviceBoxPropertyChange(evt);
            }
        });
        sServiceBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SServiceBoxActionPerformed(evt);
            }
        });
        sServiceBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                SServiceBoxPropertyChange(evt);
            }
        });
        sVariableBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SVariableBoxActionPerformed(evt);
            }
        });
        sVariableBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                SVariableBoxPropertyChange(evt);
            }
        });

        dDeviceBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DDeviceBoxActionPerformed(evt);
            }
        });
        dDeviceBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                DDeviceBoxPropertyChange(evt);
            }
        });
        dServiceBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DServiceBoxActionPerformed(evt);
            }
        });
        dServiceBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                DServiceBoxPropertyChange(evt);
            }
        });
        dVariableBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DVariableBoxActionPerformed(evt);
            }
        });
        dVariableBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                DVariableBoxPropertyChange(evt);
            }
        });
        setButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetButtonActionPerformed(evt);
            }
        });
        unSetButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnSetButtonActionPerformed(evt);
            }
        });
        //======================================
        deviceBox.setVisible(false);deviceLabel.setVisible(false);
        serviceBox.setVisible(false);serviceLabel.setVisible(false);
        actionBox.setVisible(false);actionLabel.setVisible(false);
        argumentBox.setVisible(false);argumentLabel.setVisible(false);
        valueCheckBox.setVisible(false);
        valueSpinner.setVisible(false);
        valueTextField.setVisible(false);
        callActionButton.setVisible(false);
    }
    
    private void DeviceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
        if(deviceBox.getSelectedItem()!=null){            
            deviceBox.setVisible(true);deviceLabel.setVisible(true);
            gdevice = upnpService.getRegistry().getRemoteDevice((UDN) deviceBox.getSelectedItem(), rootPaneCheckingEnabled);
    //        System.out.println(remoteDevice);            
            infoArea.setText(getInfoDevice(gdevice));
            serviceBox.removeAllItems();
            for (RemoteService service1 : gdevice.getServices()) {
    //            System.out.println(service1);
                serviceBox.addItem(service1.getServiceId().getId());
            }        
        }
        else{
            deviceBox.setVisible(false);deviceLabel.setVisible(false);
        }
    }
    
    private void ServiceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        actionBox.removeAllItems();
        if(serviceBox.getSelectedItem()!=null){
            serviceBox.setVisible(true);serviceLabel.setVisible(true);      
            gservice = gdevice.findService(new UDAServiceId(serviceBox.getSelectedItem().toString()));
//            System.out.println(gservice);
            
            if(gservice!=null){                
                for (Action action : gservice.getActions()) {
                    actionBox.addItem(action.getName());
                }
            }
        }
        else{
            serviceBox.setVisible(false);serviceLabel.setVisible(false);
        }
    }                                          

    private void ActionBoxActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
        argumentBox.removeAllItems();
        if(actionBox.getSelectedItem()!=null){
            actionBox.setVisible(true);actionLabel.setVisible(true);
            callActionButton.setVisible(true);
            gaction = gservice.getAction(actionBox.getSelectedItem().toString());            
            
            if(gaction!=null){                
                for (ActionArgument argument : gaction.getInputArguments()) {
                    argumentBox.addItem(argument.getName());
                }
            }
        }
        else{
            actionBox.setVisible(false);actionLabel.setVisible(false);
            callActionButton.setVisible(false);
        }
    }                                         

    private void ArgumentBoxActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
        
        if(argumentBox.getSelectedItem()!=null){
            argumentBox.setVisible(true);argumentLabel.setVisible(true);            
            gargument = gaction.getInputArgument(argumentBox.getSelectedItem().toString());            
            if(gargument!=null){
                Class argumentClass = gargument.getDatatype().getClass();
                if(argumentClass.equals(BooleanDatatype.class)){
                                   
                    valueSpinner.setVisible(false);
                    valueCheckBox.setVisible(true);
                    valueTextField.setVisible(false);
                }
                else{                    
                    if(argumentClass.equals(DateTimeDatatype.class)||
                       argumentClass.equals(StringDatatype.class)||
                       argumentClass.equals(CharacterDatatype.class)){
                        valueSpinner.setVisible(false);
                        valueCheckBox.setVisible(false);
                        valueTextField.setVisible(true);
                    }
                    else{
                        valueSpinner.setVisible(true);
                        valueCheckBox.setVisible(false);
                        valueTextField.setVisible(false);
                    }
                }
            }
        }
        else{
            argumentBox.setVisible(false);argumentLabel.setVisible(false);
            valueSpinner.setVisible(false);
            valueCheckBox.setVisible(false);
            valueTextField.setVisible(false);
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
            Class argumentClass = gargument.getDatatype().getClass();
            if(argumentClass.equals(BooleanDatatype.class))
                value = valueCheckBox.isSelected();
            else
                if(argumentClass.equals(DateTimeDatatype.class)||
                   argumentClass.equals(StringDatatype.class)||
                   argumentClass.equals(CharacterDatatype.class))
                    value = valueTextField.getText();
                else
                    value = valueSpinner.getValue();
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
//        System.out.println("device Box "+deviceBox.isVisible());
        serviceBox.setVisible(deviceBox.isVisible());
        deviceLabel.setVisible(deviceBox.isVisible());
    }                                        

    private void ServiceBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                          
        // TODO add your handling code here:
//        System.out.println("service Box "+serviceBox.isVisible());
        actionBox.setVisible(serviceBox.isVisible());
        serviceLabel.setVisible(serviceBox.isVisible());
    }                                         

    private void ActionBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                         
        // TODO add your handling code here:
//        System.out.println("action Box "+actionBox.isVisible());
        argumentBox.setVisible(actionBox.isVisible());
        actionLabel.setVisible(actionBox.isVisible());
    }                                        

    private void ArgumentBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                           
        // TODO add your handling code here:
//        System.out.println("argument Box "+argumentBox.isVisible());
        argumentLabel.setVisible(argumentBox.isVisible());
    }
    
    private void SVariableBoxActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
        if(sVariableBox.getSelectedItem()!=null){
            sVariableBox.setVisible(true);
            if(gdevices!=null)
                for (RemoteDevice remoteDevice : gdevices) {                
                    if(remoteDevice.getType().getType().equals(sDeviceBox.getSelectedItem())){                        
                        for (RemoteService service : remoteDevice.getServices()) {
                            if(service.getServiceType().getType().equals(sServiceBox.getSelectedItem())){
                                for (StateVariable<RemoteService> stateVariable : service.getStateVariables()) {                                    
                                    if(stateVariable.getName().equals(sVariableBox.getSelectedItem())){
                                        Class variableClass = stateVariable.getTypeDetails().getDatatype().getClass();
                                        if(variableClass.equals(BooleanDatatype.class)){                                   
                                            sSpinner.setVisible(false);
                                            sCheckBox.setVisible(true);
                                            sTextField.setVisible(false);
                                        }
                                        else{
                                            if(variableClass.equals(DateTimeDatatype.class)||
                                               variableClass.equals(StringDatatype.class)||
                                               variableClass.equals(CharacterDatatype.class)){
                                                sSpinner.setVisible(false);
                                                sCheckBox.setVisible(false);
                                                sTextField.setVisible(true);
                                            }
                                            else{
                                                sSpinner.setVisible(true);
                                                sCheckBox.setVisible(false);
                                                sTextField.setVisible(false);
                                            }
                                        }
                                        break;
                                    }                                    
                                }
                            }
                        }                   
                    }                
                }
        }
        else{
            sVariableBox.setVisible(false);
            sSpinner.setVisible(false);
            sCheckBox.setVisible(false);
            sTextField.setVisible(false);
        }
    }                                            

    private void DVariableBoxActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
        if(dVariableBox.getSelectedItem()!=null){
            dVariableBox.setVisible(true);
            if(gdevices!=null)
                for (RemoteDevice remoteDevice : gdevices) {                
                    if(remoteDevice.getType().getType().equals(dDeviceBox.getSelectedItem())){                        
                        for (RemoteService service : remoteDevice.getServices()) {
                            if(service.getServiceType().getType().equals(dServiceBox.getSelectedItem())){
                                for (StateVariable<RemoteService> stateVariable : service.getStateVariables()) {                                    
                                    if(stateVariable.getName().equals(dVariableBox.getSelectedItem())){
                                        Class variableClass = stateVariable.getTypeDetails().getDatatype().getClass();
                                        if(variableClass.equals(BooleanDatatype.class)){                                   
                                            dSpinner.setVisible(false);
                                            dCheckBox.setVisible(true);
                                            dTextField.setVisible(false);
                                        }
                                        else{
                                            if(variableClass.equals(DateTimeDatatype.class)||
                                               variableClass.equals(StringDatatype.class)||
                                               variableClass.equals(CharacterDatatype.class)){
                                                dSpinner.setVisible(false);
                                                dCheckBox.setVisible(false);
                                                dTextField.setVisible(true);
                                            }
                                            else{
                                                dSpinner.setVisible(true);
                                                dCheckBox.setVisible(false);
                                                dTextField.setVisible(false);
                                            }
                                        }
                                        break;
                                    }                                    
                                }
                            }
                        }                   
                    }                
                }
        }
        else{
            dVariableBox.setVisible(false);
            dSpinner.setVisible(false);
            dCheckBox.setVisible(false);
            dTextField.setVisible(false);
        }
    }                                            

    private void SServiceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
        sVariableBox.removeAllItems();
        if(sServiceBox.getSelectedItem()!=null){
            sServiceBox.setVisible(true);            
            if(gdevices!=null)
                for (RemoteDevice remoteDevice : gdevices) {                
                    if(remoteDevice.getType().getType().equals(sDeviceBox.getSelectedItem())){                        
                        for (RemoteService service : remoteDevice.getServices()) {
                            if(service.getServiceType().getType().equals(sServiceBox.getSelectedItem())){
                                for (StateVariable<RemoteService> stateVariable : service.getStateVariables()) {
                                    sVariableBox.addItem(stateVariable.getName());
                                }
                            }
                        }                   
                    }                
                }
        }
        else{
            sServiceBox.setVisible(false);
        }
    }                                           

    private void DServiceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
        dVariableBox.removeAllItems();
        DefaultComboBoxModel model = new DefaultComboBoxModel(new String[] {});
        if(dServiceBox.getSelectedItem()!=null){
            dServiceBox.setVisible(true);      
            if(gdevices!=null){
                for (RemoteDevice remoteDevice : gdevices) {                
                    if(remoteDevice.getType().getType().equals(dDeviceBox.getSelectedItem())){                        
                        for (RemoteService service : remoteDevice.getServices()) {
                            if(service.getServiceType().getType().equals(dServiceBox.getSelectedItem())){
                                for (Action action : service.getActions()) {                                    
                                    for (ActionArgument argument : action.getInputArguments()) {
                                        if(((DefaultComboBoxModel)dVariableBox.getModel()).getIndexOf(argument.getRelatedStateVariableName()) == -1) {
                                            dVariableBox.addItem(argument.getRelatedStateVariableName());
                                        }                                                          
                                    }
                                }                                
                            }
                        }                   
                    }                
                }
            }
        }
        else{
            dServiceBox.setVisible(false);
        }
    }
    
    private void SDeviceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        if(sDeviceBox.getSelectedItem()!=null){            
            sDeviceBox.setVisible(true);        
            gdevices = upnpService.getRegistry().getRemoteDevices();                    
            sServiceBox.removeAllItems();
            if(gdevices!=null)
                for (RemoteDevice remoteDevice : gdevices) {                
                    if(remoteDevice.getType().getType().equals(sDeviceBox.getSelectedItem())){
//                        System.out.println("Co device "+sDeviceBox.getSelectedItem()+": "+remoteDevice);
                        for (RemoteService service : remoteDevice.getServices()) {
//                            System.out.println("Co service "+service.getServiceType().getType());
                            sServiceBox.addItem(service.getServiceType().getType());
                        }                   
                    }                
                }                   
        }
        else{
            dDeviceBox.setVisible(false);
        }        
    }                                          

    private void DDeviceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        if(dDeviceBox.getSelectedItem()!=null){            
            dDeviceBox.setVisible(true);        
            gdevices = upnpService.getRegistry().getRemoteDevices();                    
            dServiceBox.removeAllItems();
            if(gdevices!=null)
                for (RemoteDevice remoteDevice : gdevices) {                
                    if(remoteDevice.getType().getType().equals(dDeviceBox.getSelectedItem())){    
                        for (RemoteService service : remoteDevice.getServices()) {
                            dServiceBox.addItem(service.getServiceType().getType());
                        }                   
                    }
                }                   
        }
        else{
            dDeviceBox.setVisible(false);
        }
    }                                          

    private void SDeviceBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                          
        // TODO add your handling code here:        
        sServiceBox.setVisible(sDeviceBox.isVisible());
    }                                         

    private void SServiceBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                           
        // TODO add your handling code here:
        sVariableBox.setVisible(sServiceBox.isVisible());
    }                                          

    private void SVariableBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void DDeviceBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                          
        // TODO add your handling code here:
        dServiceBox.setVisible(dDeviceBox.isVisible());
    }                                         

    private void DServiceBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                           
        // TODO add your handling code here:
        dVariableBox.setVisible(dServiceBox.isVisible());
    }                                          

    private void DVariableBoxPropertyChange(java.beans.PropertyChangeEvent evt) {                                            
        // TODO add your handling code here:
        
    }
    
    private void SetButtonActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
        Map<String, Object> map = new HashMap<String, Object>();
        if((sSpinner.isVisible()||sCheckBox.isVisible())&&
            (dSpinner.isVisible()||dCheckBox.isVisible())){            
            map.put("sDeviceType", sDeviceBox.getSelectedItem());
            map.put("sServiceType", sServiceBox.getSelectedItem());
            map.put("sStateVariable", sVariableBox.getSelectedItem());
            if(sSpinner.isVisible())
                map.put("sValue",sSpinner.getValue());
            else
                if(sCheckBox.isVisible())
                    map.put("sValue",sCheckBox.isSelected());
                else
                    map.put("sValue",sTextField.getText());
            map.put("dDeviceType", dDeviceBox.getSelectedItem());
            map.put("dServiceType", dServiceBox.getSelectedItem());
            map.put("dStateVariable", dVariableBox.getSelectedItem());
            if(dSpinner.isVisible())
                map.put("dValue",dSpinner.getValue());
            else
                if(dCheckBox.isVisible())
                    map.put("dValue",dCheckBox.isSelected());
                else
                    map.put("dValue",dTextField.getText());
            
            String string = "";
            if(listMap.indexOf(map)<0){
                listMap.add(map);                
                for (Map<String, Object> map1 : listMap) {
                    string += "IF ("+map1.get("sDeviceType")+","
                            +map1.get("sServiceType")+","
                            +map1.get("sStateVariable")+"=="
                            +map1.get("sValue")
                            +") THEN ("
                            +map1.get("dDeviceType")+","
                            +map1.get("dServiceType")+","
                            +map1.get("dStateVariable")+"=="
                            +map1.get("dValue")
                            +")\n";            
                }
                listArea.setText(string + "\n");
            }                        
        }
    }                                         

    private void UnSetButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
        Map<String, Object> map = new HashMap<String, Object>();
        if((sSpinner.isVisible()||sCheckBox.isVisible())&&
            (dSpinner.isVisible()||dCheckBox.isVisible())){
              
            map.put("sDeviceType", sDeviceBox.getSelectedItem());
            map.put("sServiceType", sServiceBox.getSelectedItem());
            map.put("sStateVariable", sVariableBox.getSelectedItem());
            if(sSpinner.isVisible())
                map.put("sValue",sSpinner.getValue());
            else
                if(sCheckBox.isVisible())
                    map.put("sValue",sCheckBox.isSelected());
                else
                    map.put("sValue",sTextField.getText());
            map.put("dDeviceType", dDeviceBox.getSelectedItem());
            map.put("dServiceType", dServiceBox.getSelectedItem());
            map.put("dStateVariable", dVariableBox.getSelectedItem());
            if(dSpinner.isVisible())
                map.put("dValue",dSpinner.getValue());
            else
                if(dCheckBox.isVisible())
                    map.put("dValue",dCheckBox.isSelected());
                else
                    map.put("dValue",dTextField.getText());
            
            String string = "";
            if(listMap.indexOf(map)>=0){
                listMap.remove(map);
                for (Map<String, Object> map1 : listMap) {
                    string += "IF ("+map1.get("sDeviceType")+","
                            +map1.get("sServiceType")+","
                            +map1.get("sStateVariable")+"=="
                            +map1.get("sValue")
                            +") THEN ("
                            +map1.get("dDeviceType")+","
                            +map1.get("dServiceType")+","
                            +map1.get("dStateVariable")+"=="
                            +map1.get("dValue")
                            +")\n";            
                }
                listArea.setText(string + "\n");
            }            
        }
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
                Map<String, StateVariableValue> map = new HashMap<String, StateVariableValue>();
                
                deviceBox.addItem(device.getIdentity().getUdn());                
                deviceMap.put(device, (HashMap) map);
                dDeviceBox.addItem(device.getType().getType());
                sDeviceBox.addItem(device.getType().getType());                
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
                            String deviceType,serviceType,stateVariable;
                            Object value;
                            
                            System.out.println("Event number "+sub.getCurrentSequence().getValue()+" from "+sub.getSubscriptionId());
                            Map<String, StateVariableValue> values = sub.getCurrentValues();                            
                            Map<String, StateVariableValue> deviceValues = deviceMap.get(service.getDevice());                            
                            for (Map.Entry<String, StateVariableValue> entry : values.entrySet()) {
                                String key = entry.getKey();                                                             
                                if(deviceValues.containsKey(key)){
                                    deviceValues.remove(key);                                    
                                }
                                deviceValues.put(key, entry.getValue());
                                
                                deviceType = service.getDevice().getType().getType();
                                serviceType = service.getServiceType().getType();
                                stateVariable = entry.getValue().getStateVariable().getName();
                                value = entry.getValue().getValue();
                                for (Iterator<Map<String, Object>> iterator = listMap.iterator(); iterator.hasNext();) {
                                    Map<String, Object> next = iterator.next();
                                    if(next.get("sDeviceType").equals(deviceType)&&
                                            next.get("sServiceType").equals(serviceType)&&
                                            next.get("sStateVariable").equals(stateVariable)&&
                                            next.get("sValue").equals(value)){//                                      
                                        
                                        for (RemoteDevice remoteDevice : upnpService.getRegistry().getRemoteDevices()) {
                                            if(remoteDevice.getType().getType().equals(next.get("dDeviceType"))){ 
                                                Service tmpService = remoteDevice.findService(new UDAServiceType(next.get("dServiceType").toString()));
                                                if(tmpService != null){//                                                    
                                                    for (Action action : tmpService.getActions()) {
                                                        for (ActionArgument argument : action.getInputArguments()) {
                                                            if(argument.getRelatedStateVariableName().equals(next.get("dStateVariable").toString())){//                                                                
                                                                executeAction(upnpService, tmpService, action.getName(), argument.getName(), next.get("dValue"));
                                                            }
                                                            else System.out.println("Khong the thuc thi!!!");                                                            
                                                        }
                                                    }                                                
                                                }                                                
                                            }                                            
                                        }
                                    }                           
                                }                                
                            }                                                  
                            if(service.getDevice().equals(gdevice))
                                infoArea.setText(getInfoDevice(gdevice));
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
                sDeviceBox.removeItem(device.getType().getType());
                dDeviceBox.removeItem(device.getType().getType());
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

    private String getInfoService(RemoteService service){
        String string = "";
        StateVariableValue stateVariableValue = null;
        Map<String, StateVariableValue> values = deviceMap.get(service.getDevice());
        if(service!=null){
            if(values != null)
                for (StateVariable<RemoteService> stateVariable1 : service.getStateVariables()) {
                    stateVariableValue = values.get(stateVariable1.getName());
                    if(stateVariableValue != null)
                        string += "\n       |___"+stateVariable1.getName()+": "+stateVariableValue.getValue();
                    else
                        string += "\n       |___"+stateVariable1.getName();
                }                
            else
                for (StateVariable<RemoteService> stateVariable1 : service.getStateVariables()) {
                    string += "\n       |___"+stateVariable1.getName();
                }
            return string;
        }
        else return null;
    }
    
    private String getInfoDevice(RemoteDevice device){
        String string = "";        
        if(device!=null){
            string += device.getDetails().getFriendlyName();
            for (RemoteService service : device.getServices()) {
                string += "\n|___"+service.getServiceId().getId();
                string += getInfoService(service);
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