package com.aoxing.suspensionbar

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mCurrentPosition = 0

    private var mSuspensionHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.item_jump) {
                val intent = Intent(this@MainActivity, MultiActivity::class.java)
                startActivity(intent)
            }
            false
        }

        val linearLayoutManager = LinearLayoutManager(this)
        val adapter = FeedAdapter()

        mFeedList.run {
            layoutManager = linearLayoutManager
            setAdapter(adapter)
            setHasFixedSize(true)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    mSuspensionHeight = mSuspensionBar!!.height
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val view = linearLayoutManager.findViewByPosition(mCurrentPosition + 1)
                    if (view != null) {
                        if (view.top <= mSuspensionHeight) {
                            mSuspensionBar!!.y = (-(mSuspensionHeight - view.top)).toFloat()
                        } else {
                            mSuspensionBar!!.y = 0f
                        }
                    }

                    if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
                        mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition()

                        updateSuspensionBar()
                        mSuspensionBar!!.y = 0f
                    }
                }
            })
        }

        updateSuspensionBar()
    }

    @SuppressLint("SetTextI18n")
    private fun updateSuspensionBar() {
        Log.d("HHHH", "updateSuspensionBar: $mCurrentPosition")
        Picasso.with(this@MainActivity)
                .load(getAvatarResId(mCurrentPosition))
                .centerInside()
                .fit()
                .into(mSuspensionIv)

        mSuspensionTv!!.text = "Taeyeon $mCurrentPosition"
    }

    private fun getAvatarResId(position: Int): Int {
        when (position % 4) {
            0 -> return R.drawable.avatar1
            1 -> return R.drawable.avatar2
            2 -> return R.drawable.avatar3
            3 -> return R.drawable.avatar4
        }
        return 0
    }
}
