package com.example.finanzaspersonalesnuevo.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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

    /**
     * Exporta todas las transacciones a un CSV en la carpeta pública Download.
     */
    public static void exportar(Context ctx) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Directorio público Descargas
                    File dir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS);
                    if (!dir.exists() && !dir.mkdirs()) {
                        Log.e(TAG, "No se pudo crear la carpeta Download");
                    }

                    // Nombre de archivo con timestamp
                    String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                            .format(new Date());
                    File file = new File(dir, "finanzas_export_" + ts + ".csv");
                    Log.d(TAG, "Escribiendo CSV en: " + file.getAbsolutePath());

                    // Lee todas las transacciones
                    List<Transaccion> all = AppDatabase.getInstance(ctx)
                            .transaccionDao().getAllSync();
                    Log.d(TAG, "Transacciones obtenidas: " + all.size());

                    // Construye el contenido CSV
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

                    // Escribe el fichero
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(csv.toString().getBytes());
                        Log.d(TAG, "CSV escrito con éxito");
                        return file.getAbsolutePath();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error escribiendo CSV", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String path) {
                if (path != null) {
                    Toast.makeText(ctx,
                            "✅ Exportación completada:\n" + path,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ctx,
                            "❌ Error exportando CSV",
                            Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}
