package com.isma.inmobiliaria.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.isma.inmobiliaria.ui.inicio.InicioViewModel;

public class ShakeDetector implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor acelerometro;
    private final InicioViewModel viewModel;

    private static final float ALPHA = 0.8f;
    private static final int SHAKE_THRESHOLD_GRAVITY = 12;
    private static final int SHAKE_COOLDOWN_MS = 1000;

    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private long lastShakeTime = 0;

    public ShakeDetector(Context context, InicioViewModel viewModel) {
        this.viewModel = viewModel;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            this.acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            this.acelerometro = null;
        }
    }

    public void start() {
        if (acelerometro != null) {
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("ShakeDetector", "Listener del acelerómetro REGISTRADO");
        }
    }

    public void stop() {
        if (acelerometro != null) {
            sensorManager.unregisterListener(this);
            Log.d("ShakeDetector", "Listener del acelerómetro DEREGISTRADO");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
            gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
            gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

            linearAcceleration[0] = event.values[0] - gravity[0];
            linearAcceleration[1] = event.values[1] - gravity[1];
            linearAcceleration[2] = event.values[2] - gravity[2];

            float accel = (float) Math.sqrt(
                    linearAcceleration[0] * linearAcceleration[0] +
                            linearAcceleration[1] * linearAcceleration[1] +
                            linearAcceleration[2] * linearAcceleration[2]
            );

            long currentTime = System.currentTimeMillis();
            if (accel > SHAKE_THRESHOLD_GRAVITY && (currentTime - lastShakeTime) > SHAKE_COOLDOWN_MS) {
                lastShakeTime = currentTime;
                Log.d("ShakeDetector", "¡SACUDIDA FÍSICA DETECTADA! Notificando al ViewModel...");
                viewModel.onShakeDetectado();
            }
        }
    }
}