package capstone.mukjaView.Service;


import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Domain.User;
import capstone.mukjaView.Domain.UserLikeRestaurant;
import capstone.mukjaView.Dto.ReviewPageResponse;
import capstone.mukjaView.Repository.RestaurantRepository;
import capstone.mukjaView.Repository.UserLikeRestaurantRepository;
import capstone.mukjaView.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ReviewPageResponse returnReviewPageResponse(Long restaurantId, String oauthIdentifier) {

        User user = userRepository.findByUsername(oauthIdentifier);
        if (user == null)
            return null;
        List<UserLikeRestaurant> likes = user.getLikeRestaurants();
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);

        if (restaurant.isEmpty())
            return null;
        ReviewPageResponse rtn = new ReviewPageResponse(restaurant.get());
        rtn.setEmotion(calculateEmotion(restaurant.get(), user));
        rtn.setFitMukbti(calculateMukbti(restaurant.get()));
        for (UserLikeRestaurant like : likes) {
            if (like.getRestaurant().getRestaurantId() == restaurantId) {
                rtn.setLike(true);
                break;
            }
        }
        return rtn;
    }



    static public String calculateEmotion(Restaurant restaurant, User user) {
        String mbti = user.getMukbti();

        double value = 0;
        int[] wei = {3, 2, 1};

        for (int i = 0 ; i < 3; ++i) {
            if (mbti.charAt(i) == 'F')
                value += restaurant.getFlavorRatio() * wei[i];
            else if (mbti.charAt(i) == 'M')
                value += restaurant.getMoodRatio() * wei[i];
            else if (mbti.charAt(i) == 'S')
                value += restaurant.getServiceRatio() * wei[i];
        }
        // 가성비 계산?
        // if (mbti.charAt(4) == 'R')
        // value += 2;
        if (value > 0)
            return "positive";
        else if (value < 0)
            return "negative";
        return "neutral";
    }

    static public String calculateMukbti(Restaurant restaurant) {

        double flavor = restaurant.getFlavorRatio();
        double service = restaurant.getServiceRatio();
        double mood = restaurant.getMoodRatio();

        String mukbti = "";

        if (flavor >= service && service >= mood)
            mukbti += "FSM-";
        else if (flavor >= mood && mood >= service)
            mukbti += "FMS-";
        else if (mood >= service && service >= flavor)
            mukbti += "MSF-";
        else if (mood >= flavor && flavor >= service)
            mukbti += "MFS-";
        else if (service >= mood && mood >= flavor)
            mukbti += "SMF-";
        else if (service >= flavor && flavor >= mood)
            mukbti += "SFM-";

        if (restaurant.isReasonable())
            mukbti += "R";
        else
            mukbti += "F";

        return mukbti;
    }
}
