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
package smartHome;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.types.InvalidValueException;

/**
 *
 * @author Han
 */
public class SetActionInvocation extends ActionInvocation {
    public SetActionInvocation(Action action,String argumentName,Object objectValue) {
        super(action);        
        try {
            // Throws InvalidValueException if the value is of wrong type            
            setInput(argumentName, objectValue);
        } catch (InvalidValueException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}
