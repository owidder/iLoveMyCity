package hackthedrive.bmw.de.hackthedrive.domain.rest;

import java.util.List;

import hackthedrive.bmw.de.hackthedrive.domain.rest.RestData;
import hackthedrive.bmw.de.hackthedrive.domain.rest.RouteObject;
import hackthedrive.bmw.de.hackthedrive.util.GsonDeserializer;

/**
 * Created by ewa on 10.01.2015.
 */
public class Test {
    public static void main(String [] args)
    {
        String data = "{\"PageSize\":10,\"Offset\":0,\"TotalRows\":453,\"Data\":[{\"TripId\":\"a313c719-6c62-4140-a85f-692939d6fc27\",\"Altitude\":null,\"Heading\":129,\"Distance\":6,\"FuelLevel\":5,\"FuelEfficiency\":59586,\"Speed\":19,\"Odometer\":725,\"RPM\":null,\"Range\":null,\"AcceleratorPedal\":37.75,\"BrakeTorque\":0,\"SteeringWheelAngle\":null,\"DrivingDirection\":null,\"AirConditioningOn\":null,\"CruiseControlEnabled\":null,\"Gear\":null,\"Passengers\":null,\"WheelSpeed\":null,\"Type\":\"Event\",\"MojioId\":\"62a644cf-6d01-4f9e-ace1-a2b68e799fa9\",\"VehicleId\":\"a52216dd-cfd4-4a24-9537-b33b1ad5ffec\",\"OwnerId\":\"3ce86e1c-1b33-496e-8394-859ad1105390\",\"EventType\":\"TripStatus\",\"Time\":\"2015-01-10T19:00:52.297Z\",\"Location\":{\"Lat\":37.593055,\"Lng\":-122.366036,\"FromLockedGPS\":false,\"Dilution\":0,\"IsValid\":true},\"Accelerometer\":null,\"TimeIsApprox\":true,\"BatteryVoltage\":340.5,\"ConnectionLost\":null,\"Orientation\":null,\"BatteryLevel\":null,\"BatteryCurrent\":-5.5,\"RainIntensity\":null,\"TemperatureInside\":25,\"TemperatureOutside\":null,\"_id\":\"34f91df4-2ffe-4c23-ba44-6adca49612bf\",\"_deleted\":false}]}";
        RouteObject result = GsonDeserializer.deserialize(data, RouteObject.class);
        List<RestData> data1 = result.Data;
    }
}
