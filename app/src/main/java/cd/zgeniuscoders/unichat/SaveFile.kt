package cd.zgeniuscoders.unichat

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SaveFile(val context: Context) {

    fun downloadImageFromFirestore(imageUrl: String) {
        // Créez une référence à l'emplacement de l'image dans Firebase Storage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

        // Téléchargez l'image à partir de Firebase Storage
        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            // Enregistrez l'image dans la galerie de l'utilisateur
            savePhoto(bitmap)
        }.addOnFailureListener {
            // Gérez les erreurs de téléchargement
            Toast.makeText(
                context,
                "Erreur lors du téléchargement de l'image depuis Firebase",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun savePhoto(bitmap: Bitmap) {
        // Vérifiez si le stockage externe est monté en lecture-écriture
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Toast.makeText(context, "Stockage externe non disponible", Toast.LENGTH_SHORT).show()
            return
        }

        // Créez un dossier nommé "unichat" dans le dossier des images publiques de l'utilisateur
        val imagesDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "unichat"
        )
        if (!imagesDir.exists()) {
            imagesDir.mkdir()
        }

        // Générez un nom de fichier unique pour votre image
        val filename = "my_image_${System.currentTimeMillis()}.png"

        // Créez un fichier dans le dossier "unichat"
        val image = File(imagesDir, filename)

        // Écrivez votre image dans le fichier
        val outputStream: OutputStream = FileOutputStream(image)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Ajoutez l'image à la galerie de l'utilisateur
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "my_image")
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.DESCRIPTION, "Image enregistrée par UniChat")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.DATA, image.absolutePath)
        }
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        // Affichez un message de confirmation
        Toast.makeText(context, "Image enregistrée dans UniChat", Toast.LENGTH_SHORT).show()
    }
}