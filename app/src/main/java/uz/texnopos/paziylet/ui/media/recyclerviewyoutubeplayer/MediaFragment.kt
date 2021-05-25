package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_media.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.Adapters.MediaAdapter


class MediaFragment : Fragment(R.layout.fragment_media) {
    private var screenDefaultHeight: Int=0
    private var videoSurfaceDefaultHeight: Int =0
    private val adapter : MediaAdapter by inject()
    private val viewModel:MediaViewModel by viewModel()
    private lateinit var layoutManager : LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val point = Point()
        videoSurfaceDefaultHeight = point.x
        screenDefaultHeight = point.y
        layoutManager = LinearLayoutManager(requireContext())
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        viewModel.getVideos()
        setUpObserver()
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(!recyclerView.canScrollVertically(1)){
                        playVideo(true);
                    }
                    else{
                        playVideo(false);
                    }

                }

            }
        })
    }

    private fun setUpObserver() {
        viewModel.video.observe(viewLifecycleOwner,{
            when(it.status){
                ResourceState.LOADING->{}
                ResourceState.SUCCESS->{
                    adapter.models=it.data!!
                }
                ResourceState.ERROR->{}
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun playVideo(isEndOfList: Boolean) {
        val targetPosition: Int
        if(!isEndOfList) {
            val startPosition = layoutManager.findFirstVisibleItemPosition()
            var endPosition = layoutManager.findLastVisibleItemPosition()

            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1;
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return;
            }

            // if there is more than 1 list-item on the screen
            targetPosition = if (startPosition != endPosition) {
                val startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                val endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

                if( startPositionVideoHeight > endPositionVideoHeight)
                    startPosition
                else
                    endPosition;
            } else {
                startPosition;
            }
        }
        else{
            targetPosition = adapter.models.size - 1;
        }
        (recycler_view.findViewHolderForAdapterPosition(targetPosition) as MediaAdapter.VideoPlayerViewHolder).performClick(true)
    }

    private fun getVisibleVideoSurfaceHeight(playPosition: Int): Int {
        val at = playPosition - layoutManager.findFirstVisibleItemPosition()

        val child = recycler_view.getChildAt(at) ?: return 0

        val location = IntArray(2)
        child.getLocationInWindow(location)

        return if (location[1] < 0) {
            location[1] + videoSurfaceDefaultHeight
        } else {
            screenDefaultHeight - location[1]
        }
    }

}
