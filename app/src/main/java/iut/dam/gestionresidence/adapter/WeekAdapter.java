package iut.dam.gestionresidence.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.entities.Day;
import iut.dam.gestionresidence.entities.Slot;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.DayViewHolder> {
    private final List<Day> days;

    public WeekAdapter(List<Day> days) {
        this.days = days;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day day = days.get(position);
        holder.dayOfWeekTextView.setText("Day " + (position + 1));
        updateSlotColors(holder.gridLayout, day.getSlots());
    }

    private void updateSlotColors(GridLayout gridLayout, List<Slot> slots) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View slotView = gridLayout.getChildAt(i);
            Slot slot = slots.get(i);
            Log.d("Moi", "je veux pudate: " + slot);
            int color = calculateColor(slot.getConsumptionLevel());
            slotView.setBackgroundColor(color);
        }
    }

    private int calculateColor(int consumptionLevel) {
        Log.d("Moi", "niv conso: " + consumptionLevel);
        if (consumptionLevel <= 30) {
            Log.d("Moi", "niv --: ");
            return Color.GREEN;
        } else if (consumptionLevel <= 70) {
            Log.d("Moi", "niv conso: /");
            return Color.YELLOW;
        } else {
            Log.d("Moi", "niv conso: ++");
            return Color.RED;
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        public TextView dayOfWeekTextView;
        public GridLayout gridLayout;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            gridLayout = itemView.findViewById(R.id.gridLayout);
        }
    }
}
