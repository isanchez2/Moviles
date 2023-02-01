package es.uam.eps.dadm.cards

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import es.uam.eps.dadm.cards.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {
    lateinit var binding: FragmentStatisticsBinding
    private val viewModel: StatisticsViewModel by lazy {
        ViewModelProvider(this).get(StatisticsViewModel::class.java)
    }
    private lateinit var barChart: BarChart
    private var difficultCardList = ArrayList<Int>()
    var easinessMed: Double = 0.0
    var intervalMed: Long = 0L
    var totalReviews: Int = 0
    var totalDifferentAnsweredCards: Int = 0
    var difficultCards: Int = 0
    var doubtCards: Int = 0
    var easyCards: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_statistics,
            container,
            false)
        binding.viewModel = viewModel

        /* viewModel.decks.observe(viewLifecycleOwner) {
            var message = String()
            for (deck in it)
                message += "The deck named ${deck.name} is from user ${deck.userId}\n"
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        } */

        viewModel.numDecks.observe(viewLifecycleOwner) {
            val numDecks = it
            binding.numDecks.text = numDecks.toString()
        }
        viewModel.numCards.observe(viewLifecycleOwner) {
            val numCards = it
            binding.numCards.text = numCards.toString()
        }
        viewModel.numMedEasiness.observe(viewLifecycleOwner) {
            val numMedEasiness = it
            binding.easinessMedio.text = numMedEasiness.toString()
        }
        viewModel.numMedInterval.observe(viewLifecycleOwner) {
            val numMedInterval = it
            binding.intervalMedio.text = numMedInterval.toString()
        }
        viewModel.numReviews.observe(viewLifecycleOwner) {
            val numReviews = it
            binding.totalReviews.text = numReviews.toString()
        }
        viewModel.numDifferentCardsAnswered.observe(viewLifecycleOwner) {
            val numDifferentCardsAnswered = it
            binding.differentAnsweredCards.text = numDifferentCardsAnswered.toString()
        }
        viewModel.numDifficultCards.observe(viewLifecycleOwner) {
            val difficultCards = it
            binding.difficult.text = difficultCards.toString()
        }
        viewModel.numDoubtCards.observe(viewLifecycleOwner) {
            val doubtCards = it
            binding.doubt.text = doubtCards.toString()
        }
        viewModel.numEasyCards.observe(viewLifecycleOwner) {
            val easyCards = it
            binding.easy.text = easyCards.toString()
        }

        viewModel.cards.observe(viewLifecycleOwner) {
            var totalEasiness = 0.0
            for (card in it) {
                totalEasiness += card.easiness
            }
            easinessMed = totalEasiness / it.size
            var totalInterval: Long = 0
            for (card in it) {
                totalInterval += card.interval
            }
            intervalMed = totalInterval / it.size
            for (card in it) {
                totalReviews += card.numAnswers
            }
            for (card in it) {
                if (card.answered) {
                    totalDifferentAnsweredCards += 1
                }
            }
            for (card in it) {
                if (card.quality == 0) difficultCards++
            }
            for (card in it) {
                if (card.quality == 3) doubtCards++
            }
            for (card in it) {
                if (card.quality == 5) easyCards++
            }
        }

        barChart = binding.barChart
        difficultCardList = getNumDifficultCardsList()
        initBarChart()
        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in difficultCardList.indices) {
            val score = difficultCardList[i]
            entries.add(BarEntry(i.toFloat(), score.toFloat()))
        }
        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = BarData(barDataSet)
        binding.barChart.data = data
        binding.barChart.invalidate()

        return binding.root
    }

    private fun getNumDifficultCardsList(): ArrayList<Int> {
        binding.viewModel?.numDifficultCards?.value?.let { difficultCardList.add(it) }
        binding.viewModel?.numDoubtCards?.value?.let { difficultCardList.add(it) }
        binding.viewModel?.numEasyCards?.value?.let { difficultCardList.add(it) }
        return difficultCardList
    }

    private fun initBarChart() {
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_statistics, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
            }
            R.id.log_out -> {
                Snackbar.make(requireView(), "Log out selected", Snackbar.LENGTH_SHORT).show()
            }
        }
        return true
    }
}