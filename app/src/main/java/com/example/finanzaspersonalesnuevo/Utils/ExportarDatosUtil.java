package com.example.finanzaspersonalesnuevo.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.data.AppDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportarDatosUtil {

    private static final String TAG = "ExportarDatosUtil";

    public static void exportar(Context ctx) {
        new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... voids) {
                try {
                    File dir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS);
                    if (!dir.exists() && !dir.mkdirs()) {
                        Log.e(TAG, "No se pudo crear Download");
                    }

                    String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                            .format(new Date());
                    File file = new File(dir, "finanzas_export_" + ts + ".csv");
                    Log.d(TAG, "Guardando CSV en: " + file.getAbsolutePath());

                    List<Transaccion> all = AppDatabase.getInstance(ctx)
                            .transaccionDao().getAllSync();

                    StringBuilder csv = new StringBuilder();
                    csv.append("fecha,tipo,categoria,descripcion,cantidad\n");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    for (Transaccion t : all) {
                        csv.append(sdf.format(t.getFecha())).append(",")
                                .append(t.getTipo()).append(",")
                                .append(t.getCategoria()).append(",")
                                .append(t.getDescripcion()).append(",")
                                .append(t.getCantidad()).append("\n");
                    }

                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(csv.toString().getBytes());
                        Log.d(TAG, "CSV escrito correctamente");
                        return file;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error al escribir CSV", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(File file) {
                if (file != null) {
                    String path = file.getAbsolutePath();
                    Toast.makeText(ctx,
                            "✓ Exportado: " + file.getName(),
                            Toast.LENGTH_LONG).show();
                    abrirCSV(ctx, file);
                } else {
                    Toast.makeText(ctx,
                            "⚠️ Error al exportar",
                            Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private static void abrirCSV(Context ctx, File file) {
        try {
            Uri uri = FileProvider.getUriForFile(ctx,
                    ctx.getPackageName() + ".fileprovider", file);

            Intent open = new Intent(Intent.ACTION_VIEW);
            open.setDataAndType(uri, "text/csv");
            open.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            ctx.startActivity(Intent.createChooser(open, "Abrir con…"));
        } catch (Exception e) {
            Log.e(TAG, "No se pudo abrir CSV", e);
            Toast.makeText(ctx,
                    "No se pudo abrir el archivo.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
