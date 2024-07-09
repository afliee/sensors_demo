package com.example.sensor_demo.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Gyroscope {
//    defind method will be called when the sensor value changes
    public interface Listener {
        void onRotation(float x, float y, float z);
    }
    private Listener listener;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;

    public Gyroscope(Context context) {
//        Get the sensor manager
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

//        Get the gyroscope
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

//        Create a listener to listen to the gyroscope changes
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (listener != null) {
                    listener.onRotation(event.values[0], event.values[1], event.values[2]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

//    Register the listener
    public void register() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

//    Unregister the listener
    public void unregister() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
