package smartHome;
import java.awt.PopupMenu;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import static org.fourthline.cling.binding.xml.Descriptor.Device.ELEMENT.service;
import org.fourthline.cling.controlpoint.*;
import org.fourthline.cling.model.action.*;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
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
    private final javax.swing.JTextArea cpArea;
    private final javax.swing.JComboBox actionBox;
    private final javax.swing.JComboBox argumentBox;    
    private final javax.swing.JButton callActionButton;    
    private final javax.swing.JComboBox serviceBox;
    private final javax.swing.JComboBox deviceBox;
    private final UpnpService upnpService;
    private RemoteDevice gdevice;
    private Service gservice;
    private Action gaction;
    private ActionArgument gargument;
    private int abc;
    public ControlPoint(){
        upnpService = new UpnpServiceImpl();
        cpArea = this.getCPArea();
        deviceBox = this.getDeviceBox();
        serviceBox = this.getServiceBox();        
        actionBox = this.getActionBox();
        argumentBox = this.getArgumentBox();
        callActionButton = this.getCallActionButton();
        abc=0;
        
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
        
    }
    private void DeviceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:        
        gdevice = upnpService.getRegistry().getRemoteDevice((UDN) deviceBox.getSelectedItem(), rootPaneCheckingEnabled);
//        System.out.println(remoteDevice);
        serviceBox.removeAllItems();
        for (RemoteService service1 : gdevice.getServices()) {
//            System.out.println(service1);
            serviceBox.addItem(service1.getServiceId().getId());
        }        
    }                                         

    private void ServiceBoxActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        System.out.println("Hien deviec la "+ evt.getModifiers());
        if(evt.getModifiers()!=0){
            gservice = gdevice.findService(new UDAServiceId(serviceBox.getSelectedItem().toString()));
            System.out.println(gservice);
    //        if(gservice!=null){
    //            for (Action action : gservice.getActions()) {
    //                actionBox.addItem(action.getName());
    //            }
    //        }
        }
    }                                          

    private void ActionBoxActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void ArgumentBoxActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }
    private void CallActionButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
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
            System.out.println("Hien tai :"+getDevices());

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
                cpArea.append(device.getDetails().getFriendlyName()+"\n");
                deviceBox.addItem(device.getIdentity().getUdn());                
//                for (Map.Entry<Object, Object> entry : getDevices()) {
//                    Object key = entry.getKey();
//                    Object value = entry.getValue();
//                    
//                }
//                deviceBox.setModel(new DefaultComboBoxModel(getDevices()));
//                   for (Object device1 : getDevices()) {
//                       System.out.println((RemoteDevice)device1.ge); 
//                    }
                 
//                registry.addDevice(device);
//                System.out.println("resgis co: "+registry.getRemoteDevices().size());
//                if ((service = device.findService(serviceId)) != null) {                    
//                    System.out.println("Service discovered: " + service);
////                    executeAction(upnpService, service, "SetVolume", "NewVolumeValue", 33);
////                    executeAction(upnpService, service, "SetStatus", "NewStatusValue", true);
//                    cpArea.append(service.getServiceId().toString()+"\n");
//                    SubscriptionCallback callback = new SubscriptionCallback(service, 600) { // Timeout in seconds
//
//                        @Override
//                        public void established(GENASubscription sub) {
//                            System.out.println("Established: " + sub.getSubscriptionId());
//                        }
//
//                        @Override
//                        public void failed(GENASubscription sub, UpnpResponse response, Exception ex) {
//                            System.err.println(
//                                createDefaultFailureMessage(response, ex)
//                            );
//                        }
//
//                        @Override
//                        public void ended(GENASubscription sub, CancelReason reason, UpnpResponse response) {
//                            // Reason should be null, or it didn't end regularly
//                        }
//
//                        @Override
//                        public void eventReceived(GENASubscription sub) {
////                            System.out.println(abc);
////                            System.out.println("Event: " + sub.getCurrentSequence().getValue());
//                            Map<String, StateVariableValue> values = sub.getCurrentValues();
////                            System.out.println(sub);
//                            StateVariableValue status = values.get("Status");
//                            
//                            
//                            if(status!=null) System.out.println("Status is: " + status.toString());
//                            else{
//                                System.out.println("Ko co status.");
//                            }
//                        }
//
//                        @Override
//                        public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
//                            System.out.println("Missed events: " + numberOfMissedEvents);
//                        }
//
//                        @Override
//                        protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
//                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                        }
//                   };
//                   upnpService.getControlPoint().execute(callback);
//                }
//                else System.out.println("Khong tim thay service "+ serviceId);
            }
            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
//                Service service;
//                if ((service = device.findService(serviceId)) != null) {                    
//                    System.out.println("Service disappeared: " + service);                    
//                    cpArea.setText(cpArea.getText().replace(service.getServiceId().toString()+"\n", ""));
//                }
                  System.out.println("Remove : "+ device);
            }
            private void executeAction(
                    UpnpService upnpService,
                    Service service,
                    String actionName,
                    String agrumentName,
                    Object value
            ) {
                // Begin check params xem co hop le hay khong, hop le thi moi execute action:
                Action action = service.getAction(actionName);
                boolean paramsOk = false;
                if(action != null){
                    ActionArgument argument = action.getInputArgument(agrumentName);
                    if(argument != null){                        
                        if(argument.getDatatype().isHandlingJavaType(value.getClass())){
                            System.out.println("OK ALL");
                            paramsOk = true;
                        }
                        else System.out.println("ValueType is invalid.");
                    }
                    else System.out.println("ArgumentName is invalid.");                    
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
                                System.out.println("Successfully called action!");
                            }
                            @Override
                            public void failure(ActionInvocation invocation,UpnpResponse operation,String defaultMsg) {
                                System.err.println(defaultMsg);
                            }
                        }
                    );
                }else System.out.println("=>Params is invalid");
            }           
        };
    }
    public Collection<RemoteDevice> getDevices(){            
        return upnpService.getRegistry().getRemoteDevices();
    }
}
class CallAction extends ActionInvocation {
    public CallAction(Service service,String actionName,String agrumentName,Object value) {
        super(service.getAction(actionName));        
        try {
            // Throws InvalidValueException if the value is of wrong type
            setInput(agrumentName,value);          
            System.out.println("Called: "+actionName+">"+agrumentName+">"+String.valueOf(value));
        } catch (InvalidValueException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}