package com.example.firebasecommunityapp.presentation.view.signup.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.firebasecommunityapp.R
import com.example.firebasecommunityapp.data.model.UserData
import com.example.firebasecommunityapp.databinding.FragmentSetProfileBinding
import com.example.firebasecommunityapp.presentation.view.signup.SignUpViewmodel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern


@Suppress("DEPRECATION")
@AndroidEntryPoint
class SetProfileFragment : Fragment() {
    private var proFileUri: Uri? = null
    private val signInViewModel by activityViewModels<SignUpViewmodel>()
    private lateinit var binding: FragmentSetProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_profile, container, false)

        binding.startMain = this


        signInViewModel.checkUserPictureIs.observe(
            viewLifecycleOwner, {
                signInViewModel.checkUserNicknameIs.observe(
                    viewLifecycleOwner, {
                        binding.button.setBackgroundResource(R.color.backcolor)
                    }
                )
            }
        )

        return binding.root

    }

    fun getUserProfileImage() {
        val intent = Intent()

        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        ImagePicker.with(this)
            .compress(1024)
            .start()
    }

    //?????? ???????????? ????????? ???????????????.
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            2404 -> {
                proFileUri = data?.data!!

                binding.profileImageView.setImageURI(proFileUri)

                signInViewModel.checkProfileIs()
            }
            ImagePicker.RESULT_ERROR -> {
                proFileUri = null
                Toast.makeText(
                    requireContext(),
                    "??????????????? ???????????????. ???????????? ???????????? ??????????????????",
                    Toast.LENGTH_SHORT
                )
                    .show()

                signInViewModel.checkNickNameProfileChange()
            }
            else -> {
                Toast.makeText(requireContext(), "?????? ?????????. ???????????? ???????????? ??????????????????", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun nickNameCheck() {

        if (TextUtils.isEmpty(binding.nicknameEditText.text)) {
            Toast.makeText(activity, "???????????? ??????????????????!", Toast.LENGTH_SHORT).show()
        } else {
            signInViewModel.nicknameCheckInfo().addOnSuccessListener {
                for (i in 0 until it.size()) {
                    if (it.documents[i].id == binding.nicknameEditText.text.toString()) {
                        Toast.makeText(
                            activity,
                            "?????? ???????????? ????????? ?????????. ?????? ???????????? ??????????????????!",
                            Toast.LENGTH_SHORT
                        ).show()
                        signInViewModel.checkNickNameProfileChange()
                    }
                    else if (!Pattern.matches("^[???-??????-???a-zA-Z0-9._-]{2,}\$", binding.nicknameEditText.text))
                    {
                        Toast.makeText(
                            activity,
                            "????????? ???????????? id ?????????. ????????? ?????? ?????? ??????????????????",
                            Toast.LENGTH_SHORT
                        ).show()
                        signInViewModel.checkNickNameProfileChange()
                    }
                    else{
                        Toast.makeText(activity, "??????????????? ????????? ?????????.", Toast.LENGTH_SHORT).show()
                        signInViewModel.checkNickNameIs()
                    }
                }
            }
        }
    }

        fun getNickname() {
            if (signInViewModel.checkUserNicknameIs.value == true && signInViewModel.checkUserPictureIs.value == true) {
                signInViewModel.getNicknameAndProfileP(
                    binding.nicknameEditText.text.toString(),
                    binding.profileImageView.toString()
                )

                signInViewModel.saveUserInformation(UserData(
                    signInViewModel.phoneNumber.value!!,
                    signInViewModel.nickName.value!!,
                    signInViewModel.password.value!!,
                    signInViewModel.id.value!!,
                    signInViewModel.profilePicture.value!!))

                requireActivity().finish()
            } else {
                Toast.makeText(activity, "???????????? ????????? ??????????????????!", Toast.LENGTH_SHORT).show()
            }
        }

    }