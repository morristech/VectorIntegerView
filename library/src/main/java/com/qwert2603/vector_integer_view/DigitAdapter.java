package com.qwert2603.vector_integer_view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.math.BigInteger;

class DigitAdapter extends RecyclerView.Adapter<DigitAdapter.DigitViewHolder> {

    @ColorInt
    private int digitColor = Color.BLACK;

    @NonNull
    private BigInteger mInteger = BigInteger.ZERO;

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public DigitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viv_item_digit, parent, false);
        ImageView imageView = view.findViewById(R.id.viv_img);
        imageView.setColorFilter(digitColor, PorterDuff.Mode.SRC_ATOP);
        return new DigitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DigitViewHolder holder, int position) {
        holder.setDigit(Utils.getDigitAt(mInteger, position));
        holder.setDigitColor(digitColor);
    }

    @Override
    public int getItemCount() {
        return Utils.getDigitCount(mInteger);
    }

    @NonNull
    BigInteger getInteger() {
        return mInteger;
    }

    void setInteger(@NonNull final BigInteger integer) {
        final BigInteger old = mInteger;
        mInteger = integer;
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return Utils.getDigitCount(old);
            }

            @Override
            public int getNewListSize() {
                return Utils.getDigitCount(integer);
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                if (oldItemPosition == 0 && Utils.getDigitAt(old, 0) == VectorIntegerView.DIGIT_MINUS) {
                    return Utils.getDigitAt(integer, newItemPosition) == VectorIntegerView.DIGIT_MINUS;
                }
                if (newItemPosition == 0 && Utils.getDigitAt(integer, 0) == VectorIntegerView.DIGIT_MINUS) {
                    return Utils.getDigitAt(old, oldItemPosition) == VectorIntegerView.DIGIT_MINUS;
                }
                return getOldListSize() - oldItemPosition == getNewListSize() - newItemPosition;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return Utils.getDigitAt(old, oldItemPosition) == Utils.getDigitAt(integer, newItemPosition);
            }
        }, true).dispatchUpdatesTo(this);
    }

    @ColorInt
    public int getDigitColor() {
        return digitColor;
    }

    public void setDigitColor(@ColorInt int digitColor) {
        this.digitColor = digitColor;
        notifyItemRangeChanged(0, getItemCount());
    }

    private static final int[] ATTRS = {
            R.attr.viv_state_zero,
            R.attr.viv_state_one,
            R.attr.viv_state_two,
            R.attr.viv_state_three,
            R.attr.viv_state_four,
            R.attr.viv_state_five,
            R.attr.viv_state_six,
            R.attr.viv_state_seven,
            R.attr.viv_state_eight,
            R.attr.viv_state_nine,
            R.attr.viv_state_nth,
            R.attr.viv_state_minus,
    };

    static class DigitViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        @IntRange(from = 0, to = VectorIntegerView.MAX_DIGIT)
        int d;

        DigitViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.viv_img);
        }

        void setDigit(@IntRange(from = 0, to = VectorIntegerView.MAX_DIGIT) int digit) {
            d = digit;

            int[] state = new int[ATTRS.length];

            for (int i = 0; i < ATTRS.length; i++) {
                if (i == digit) {
                    state[i] = ATTRS[i];
                } else {
                    state[i] = -ATTRS[i];
                }
            }

            mImageView.setImageState(state, true);
        }

        void setDigitColor(@ColorInt int color) {
            mImageView.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

}
