package es.uam.eps.dadm.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.cards.databinding.ListItemDeckBinding

class DeckAdapter : RecyclerView.Adapter<DeckAdapter.DeckHolder>() {
    var data = listOf<Deck>()
    lateinit var binding: ListItemDeckBinding

    inner class DeckHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var local = binding
        fun bind(deck: Deck) {
            local.deck = deck
            itemView.setOnClickListener {
                it.findNavController()
                    .navigate(DeckListFragmentDirections.actionDeckListFragmentToDeckEditFragment(deck.deckId))
            }
            binding.listItemCardsDeck.setOnClickListener {
                it.findNavController()
                    .navigate(DeckListFragmentDirections.actionDeckListFragmentToCardListFragment(deck.deckId))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckAdapter.DeckHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemDeckBinding.inflate(layoutInflater, parent, false)
        return DeckHolder(binding.root)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DeckAdapter.DeckHolder, position: Int) {
        holder.bind(data[position])
    }
}