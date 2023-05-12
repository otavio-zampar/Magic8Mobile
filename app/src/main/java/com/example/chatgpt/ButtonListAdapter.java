public class ButtonListAdapter extends RecyclerView.Adapter<ButtonListAdapter.ViewHolder> {

    private List<String> buttonLabels;

    public ButtonListAdapter(List<String> buttonLabels) {
        this.buttonLabels = buttonLabels;
    }

    public void addButton(String label) {
        buttonLabels.add(label);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String label = buttonLabels.get(position);
        holder.button.setText(label);
    }

    @Override
    public int getItemCount() {
        return buttonLabels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }
    }
}
