package com.example.finanzaspersonalesnuevo.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.data.AppDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExportarDatosUtil {

    /**
     * Inicia la exportación en segundo plano.
     */
    public static void exportar(Context ctx) {
        new ExportTask(ctx).execute();
    }

    private static class ExportTask extends AsyncTask<Void, Void, String> {
        private Context context;

        ExportTask(Context ctx) {
            // Utiliza el contexto de aplicación para evitar fugas de memoria
            this.context = ctx.getApplicationContext();
        }

        @Override
        protected String doInBackground(Void... voids) {
            List<Transaccion> transacciones = AppDatabase
                    .getInstance(context)
                    .transaccionDao()
                    .getAllSync();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            StringBuilder csv = new StringBuilder();
            csv.append("fecha,tipo,categoria,descripcion,cantidad\n");

            for (Transaccion t : transacciones) {
                String fecha = sdf.format(t.getFecha());
                csv.append(fecha).append(",")
                        .append(t.getTipo()).append(",")
                        .append(t.getCategoria()).append(",")
                        .append(t.getDescripcion()).append(",")
                        .append(t.getCantidad()).append("\n");
            }

            File file = new File(context.getExternalFilesDir(null), "finanzas_export.csv");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(csv.toString().getBytes());
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String path) {
            if (path != null) {
                Toast.makeText(context, "Exportado a: " + path, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Error al exportar", Toast.LENGTH_LONG).show();
            }
        }
    }
}