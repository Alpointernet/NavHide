package com.alp.navhide;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import rikka.shizuku.Shizuku;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SHIZUKU = 1002;

    private Button hideButton;
    private Button showButton;
    private TextView statusText;

    private final Shizuku.OnBinderReceivedListener binderReceivedListener =
            () -> runOnUiThread(this::handleShizukuReady);

    private final Shizuku.OnBinderDeadListener binderDeadListener =
            () -> runOnUiThread(() -> statusText.setText("Shizuku disconnected"));

    private final Shizuku.OnRequestPermissionResultListener permissionResultListener =
            (requestCode, grantResult) -> {
                if (requestCode == REQUEST_SHIZUKU) {
                    runOnUiThread(() -> statusText.setText(
                            grantResult == PackageManager.PERMISSION_GRANTED
                                    ? "Shizuku permission granted — ready"
                                    : "Shizuku permission denied"));
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideButton = findViewById(R.id.hideButton);
        showButton = findViewById(R.id.showButton);
        statusText = findViewById(R.id.statusText);

        hideButton.setOnClickListener(v -> hideNavigationBar());
        showButton.setOnClickListener(v -> showNavigationBar());

        statusText.setText("Waiting for Shizuku...");

        Shizuku.addBinderReceivedListenerSticky(binderReceivedListener);
        Shizuku.addBinderDeadListener(binderDeadListener);
        Shizuku.addRequestPermissionResultListener(permissionResultListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Shizuku.pingBinder()) handleShizukuReady();
        else statusText.setText("Shizuku is not connected");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shizuku.removeBinderReceivedListener(binderReceivedListener);
        Shizuku.removeBinderDeadListener(binderDeadListener);
        Shizuku.removeRequestPermissionResultListener(permissionResultListener);
    }

    private void hideNavigationBar() {
        if (!ensureShizuku()) return;

        StringBuilder sb = new StringBuilder();

        String r1 = runShizukuCommand(new String[]{
                "cmd", "statusbar", "disable-for-setup", "true"
        });
        String r2 = runShizukuCommand(new String[]{
                "cmd", "statusbar", "send-disable-flag", "home", "recents"
        });

        if (r1 == null && r2 == null) {
            sb.append("Navigation Bar Hidden Successfully!");
        } else {
            sb.append("Command Output:\n")
              .append(r1 != null ? "disable-for-setup: " + r1.trim() + "\n" : "")
              .append(r2 != null ? "send-disable-flag: " + r2.trim() : "");
        }

        statusText.setText(sb.toString());
    }

    private void showNavigationBar() {
        if (!ensureShizuku()) return;

        runShizukuCommand(new String[]{"cmd", "statusbar", "send-disable-flag", "none"});
        runShizukuCommand(new String[]{"cmd", "statusbar", "disable-for-setup", "false"});

        statusText.setText("Navigation Bar Restored Successfully!");
    }

    private boolean ensureShizuku() {
        if (!Shizuku.pingBinder()) { showShizukuDialog("Shizuku is not running."); return false; }
        if (Shizuku.isPreV11()) { statusText.setText("Shizuku too old (needs v11+)"); return false; }
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            showShizukuDialog("Shizuku permission required.");
            Shizuku.requestPermission(REQUEST_SHIZUKU);
            return false;
        }
        return true;
    }

    private String runShizukuCommand(String[] cmd) {
        try {
            java.lang.reflect.Method newProcess = Shizuku.class.getDeclaredMethod(
                    "newProcess", String[].class, String[].class, String.class);
            newProcess.setAccessible(true);
            Process p = (Process) newProcess.invoke(null, cmd, null, null);
            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = out.readLine()) != null) sb.append(line).append('\n');
            while ((line = err.readLine()) != null) sb.append(line).append('\n');
            p.waitFor();
            String s = sb.toString().trim();
            return s.isEmpty() ? null : s;
        } catch (Throwable t) { return "Error: " + t.getMessage(); }
    }

    private void showShizukuDialog(String reason) {
        new AlertDialog.Builder(this)
                .setTitle("Shizuku required")
                .setMessage(reason)
                .setPositiveButton("Open Shizuku", (d, w) -> {
                    Intent l = getPackageManager().getLaunchIntentForPackage("moe.shizuku.privileged.api");
                    if (l != null) startActivity(l);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void handleShizukuReady() {
        if (Shizuku.isPreV11()) { statusText.setText("Shizuku too old (needs v11+)"); return; }
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED)
            statusText.setText("Shizuku is ready");
        else {
            statusText.setText("Requesting Shizuku permission...");
            Shizuku.requestPermission(REQUEST_SHIZUKU);
        }
    }
}
