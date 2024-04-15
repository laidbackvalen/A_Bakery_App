package thebakingbreak.seller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import thebakingbreak.seller.databinding.FragmentHomeBinding;
import thebakingbreak.seller.fragments.recent.RecentOrderFragment;
import thebakingbreak.seller.fragments.recent.RecentProductFragment;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    FirebaseAuth auth;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();

//        binding.imgMenu.setOnClickListener(v -> {
//            PopupMenu menu = new PopupMenu(getContext(),binding.imgMenu);
//            menu.getMenu().add("Profile")
//                    .setOnMenuItemClickListener(item -> {
//                        startActivity(new Intent(getContext(), ProfileActivity.class));
//                        return false;
//                    });
//
//            menu.getMenu().add("Logout")
//                    .setOnMenuItemClickListener(item -> {
//                        new UserPreferences(getContext()).clear();
//                        auth.signOut();
//                        startActivity(new Intent(getContext(), LoginActivity.class));
//                        getActivity().finish();
//                        return false;
//                    });
//            menu.show();
//        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFragment(binding.frameRecentProduct,new RecentProductFragment());
        setFragment(binding.frameRecentOrder,new RecentOrderFragment());
    }


    private void setFragment(FrameLayout layout,Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(layout.getId(), fragment)
                .commit();
    }


}