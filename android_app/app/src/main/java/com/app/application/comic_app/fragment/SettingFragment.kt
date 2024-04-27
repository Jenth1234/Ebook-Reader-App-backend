package com.app.application.comic_app.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.AlertDialog
import com.app.application.comic_app.R
import com.app.application.comic_app.databinding.FragmentSettingBinding
import com.app.application.comic_app.intro.SplashActivity
import com.app.application.comic_app.settings.UpdateProfileActivity

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingBinding.inflate(layoutInflater)

        init()
        onClick()

        return binding.root
    }

    private fun init() {
    }

    private fun onClick() {
        binding.buttonUpdateAccount.setOnClickListener {
            val intent = Intent(requireActivity(), UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        binding.buttonChat.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://www.facebook.com/profile.php?id=100080944372024&mibextid=LQQJ4d"))
            startActivity(intent)
        }

        binding.buttonPolicy.setOnClickListener {
            // button dieu khoan thi anh mo den mot link policy default nhe
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://www.websense.com/content/support/library/web/v80/triton_web_help/policies.aspx"))
            startActivity(intent)
        }

        binding.buttonThongTin.setOnClickListener {
            Toast.makeText(requireActivity(), "Dien thong tin muon hien thi vao day!" +
                    "", Toast.LENGTH_LONG).show()
        }

        binding.buttonLogout.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle("Xác nhận đăng xuất!")
                .setMessage("Bạn có muốn đăng xuất tài khoản khỏi thiết bị?")
                .setNegativeButton("Có", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intet = Intent(requireActivity(), SplashActivity::class.java)
                        startActivity(intet)
                        requireActivity().finishAffinity()
                    }
                })
                .setPositiveButton("Khong", null)
                .show()
        }
    }
}