package com.example.sensor_demo.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer {
    public interface Listener {
        void onTranslation(float x, float y, float z);
    }

    private Listener listener;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;

    public Accelerometer(Context context) {
//        Get the sensor manager
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

//        Get the accelerometer
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

//        Create a listener to listen to the accelerometer changes
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (listener != null) {
                    listener.onTranslation(event.values[0], event.values[1], event.values[2]);
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
