package falih.example.bukuwarungtestapp.ui.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import falih.example.bukuwarungtestapp.BuildConfig
import falih.example.bukuwarungtestapp.R
import falih.example.bukuwarungtestapp.databinding.FragmentProfileBinding
import id.zelory.compressor.Compressor
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {

    companion object {
        private const val REQUEST_CAMERA = 100
    }

    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var binding: FragmentProfileBinding
    private var tmpPhotoFilePath: String? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initView()
        initObserver()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun initView() {
        binding.ivAvatar.setOnClickListener {
            requestStoragePermission()
        }
    }

    private fun initObserver() {
        viewModel.avatarPath.observe(viewLifecycleOwner, Observer {
            it?.let {
                Glide.with(requireContext())
                    .load(it)
                    .into(binding.ivAvatar)
            }
        })
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {
            if(tmpPhotoFilePath == null) return
            val fileTempPathWithoutFileName: String =
                tmpPhotoFilePath!!.substring(0, tmpPhotoFilePath!!.lastIndexOf("/") + 1)
            val timeStamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
            ).format(Date())
            val fixedFile = File(fileTempPathWithoutFileName, "ava_$timeStamp.jpg")

            val tmpFile = File(tmpPhotoFilePath!!)
            tmpFile.renameTo(fixedFile)
            Glide.with(requireContext())
                .load(fixedFile.absolutePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.ivAvatar)

            viewModel.setAvatarImagePath(fixedFile.absolutePath)
        }
    }

    private fun moveAndCompress(
        inputPath: String,
        outputPath: String
    ): String? {
        var newFilePath: String? = null
        val oldFile = File(inputPath)
        try {
            val compressedImage = Compressor(requireContext())
                .setMaxWidth(800)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(outputPath)
                .compressToFile(oldFile)
            // delete the original file
            oldFile.delete()
            newFilePath = compressedImage.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return newFilePath
    }

    private fun requestStoragePermission() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // check if all permissions are granted
                    if (report?.areAllPermissionsGranted() == true) {
                        openCamera()
                    }
                    // check for permanent denial of any permission
                    if (report?.isAnyPermissionPermanentlyDenied == true) {
                        // show alert dialog navigating to Settings
                        showSettingsDialog();
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .withErrorListener {
                Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()
            .check()

    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            //Create a file to store the image
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // handle error
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile
                )
                takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    photoURI
                )
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    takePictureIntent.clipData = ClipData.newRawUri("", photoURI)
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivityForResult(takePictureIntent, REQUEST_CAMERA)
            }
        } else {
            Toast.makeText(requireContext(), "No camera available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Need Permissions")
        builder.setMessage(
            "This app needs permission to use this feature. You can grant them in app settings."
        )
        builder.setPositiveButton("GOTO SETTINGS") { dialog, _ ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val mFileName = "avatar_tmp"
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(mFileName, ".jpg", storageDir)
        tmpPhotoFilePath = image.absolutePath
        return image
    }
}