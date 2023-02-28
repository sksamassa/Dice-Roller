package com.example.diceroller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.diceroller.databinding.FragmentGameBinding

@Suppress("DEPRECATION")
class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
     binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        //Lets inform the system about our appbar is participating in population of the options menu
        setHasOptionsMenu(true)

        var sum = 0
        binding.rollButton.setOnClickListener {
            view: View ->
            sum += rollDice()
            binding.totalText.text = "Total: $sum"
            if(sum == 36){
                //You've won the game :-)
                view.findNavController().navigate(R.id.action_gameFragment_to_gameWonFragment)

            }else if(sum > 36){
                //Unfortunately, you should try again.
                view.findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun rollDice(): Int {

        binding.apply {
            invalidateAll()
            val points: Int
            when ((1..6).random()) {
                1 -> {
                    points = 1
                    diceImage.setImageResource(R.drawable.dice_1)
                }
                2 -> {
                    points = 2
                    diceImage.setImageResource(R.drawable.dice_2)
                }
                3 -> {
                    points = 3
                    diceImage.setImageResource(R.drawable.dice_3)
                }
                4 -> {
                    points = 4
                    diceImage.setImageResource(R.drawable.dice_4)
                }
                5 -> {
                    points = 5
                    diceImage.setImageResource(R.drawable.dice_5)
                }
                else -> {
                    points = 6
                    diceImage.setImageResource(R.drawable.dice_6)
                }
            }

            return points
        }
    }

    private fun getShareIntent(): Intent {

        val totalView = binding.totalText
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_mood))

        return shareIntent
    }

    //Starting an activity with our new Intent(sharing)
    private fun shareMood(){
        startActivity(getShareIntent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.share_menu, menu)
        //Check if the activity resolves
        if(null == getShareIntent().resolveActivity(requireActivity().packageManager)){
            menu?.findItem(R.id.share)?.isVisible = false
        }
    }

    //Sharing from the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.share -> shareMood()
        }
        @Suppress("DEPRECATION")
        return super.onOptionsItemSelected(item)
    }
}