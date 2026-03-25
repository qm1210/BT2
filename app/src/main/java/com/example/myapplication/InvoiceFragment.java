package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.databinding.FragmentInvoiceBinding;

import java.util.List;
import java.util.Locale;

public class InvoiceFragment extends BaseFragment<FragmentInvoiceBinding> {

    private static final String ARG_ORDER_ID = "order_id";
    private int orderId;
    private AppDatabase db;

    public static InvoiceFragment newInstance(int orderId) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getInt(ARG_ORDER_ID);
        }
    }

    @Override
    protected FragmentInvoiceBinding inflateBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentInvoiceBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        db = AppDatabase.getInstance(requireContext());

        binding.btnBackToHome.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });
    }

    @Override
    protected void initData() {
        Order order = db.orderDao().getOrderById(orderId);
        if (order != null) {
            binding.tvInvoiceId.setText("Mã đơn hàng: #" + order.getId());
            binding.tvInvoiceDate.setText("Ngày đặt: " + order.getOrderDate());

            List<OrderDetail> details = db.orderDetailDao().getOrderDetailsByOrderId(orderId);
            StringBuilder sb = new StringBuilder();
            double total = 0;
            
            for (OrderDetail detail : details) {
                Product product = db.productDao().getProductById(detail.getProductId());
                if (product != null) {
                    double itemTotal = product.getPrice() * detail.getQuantity();
                    sb.append(String.format(Locale.getDefault(), "%-20s x %2d = $%.2f\n", 
                            product.getName(), detail.getQuantity(), itemTotal));
                    total += itemTotal;
                }
            }
            
            binding.tvInvoiceDetails.setText(sb.toString());
            binding.tvInvoiceTotal.setText(String.format(Locale.getDefault(), "$%.2f", total));
        }
    }
}
