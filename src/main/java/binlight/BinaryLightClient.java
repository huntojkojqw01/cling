/*
 * Copyright (C) 2017 4th Line GmbH, Switzerland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package binlight;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.*;
import org.fourthline.cling.model.action.*;
import org.fourthline.cling.model.message.*;
import org.fourthline.cling.model.message.header.*;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.*;
import org.fourthline.cling.registry.*;
/**
 *
 * @author Han
 */
public class BinaryLightClient implements Runnable{
    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread clientThread = new Thread(new BinaryLightClient());
        clientThread.setDaemon(false);
        clientThread.start();

    }

    @Override
    public void run() {
        try {

            UpnpService upnpService = new UpnpServiceImpl();

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

                ServiceId serviceId = new UDAServiceId("SwitchPower");

                @Override
                public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

                    Service switchPower;
                    if ((switchPower = device.findService(serviceId)) != null) {

                        System.out.println("Service discovered: " + switchPower);
                        executeAction(upnpService, switchPower);

                    }

                }

                @Override
                public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                    Service switchPower;
                    if ((switchPower = device.findService(serviceId)) != null) {
                        System.out.println("Service disappeared: " + switchPower);
                    }
                }

            private void executeAction(UpnpService upnpService, Service switchPowerService) {

            ActionInvocation setTargetInvocation =
                    new SetTargetActionInvocation(switchPowerService);

            // Executes asynchronous in the background
            upnpService.getControlPoint().execute(
                    new ActionCallback(setTargetInvocation) {

                        @Override
                        public void success(ActionInvocation invocation) {
                            assert invocation.getOutput().length == 0;
                            System.out.println("Successfully called action!");
                        }

                        @Override
                        public void failure(ActionInvocation invocation,
                                            UpnpResponse operation,
                                            String defaultMsg) {
                            System.err.println(defaultMsg);
                        }
                    }
            );

    }

        };
    }
}
