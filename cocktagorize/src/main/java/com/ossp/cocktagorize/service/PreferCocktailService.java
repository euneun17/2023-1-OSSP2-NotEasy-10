package com.ossp.cocktagorize.service;

import com.ossp.cocktagorize.data.dto.CocktailResponseDto;
import com.ossp.cocktagorize.data.entity.*;
import com.ossp.cocktagorize.data.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PreferCocktailService {
    @Autowired
    private CocktailTagRepository cocktailTagRepository;

    @Autowired
    private CocktailRepository cocktailRepository;
    @Autowired
    private PreferTagRepository preferTagRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLikeCocktailRepository userLikeCocktailRepository;

    @Transactional
    public List<CocktailResponseDto> getPreferTagCocktail(String username){
        List<CocktailResponseDto> preferCocktail=new ArrayList<>();
        User user=userRepository.findByUsername(username);//유저이름으로 유저 찾기
        List<PreferTag> taglist=preferTagRepository.findPreferTagsByUserId(user.getId());
        List<CocktailTag> tags=new ArrayList<>();
        for(PreferTag tag:taglist){
            tags.addAll(cocktailTagRepository.findCocktailTagsByTagId(tag.getTag().getId()));
        }
        Random random=new Random();
        random.setSeed(System.currentTimeMillis());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for(int i=0;i<3;i++){
            int n=random.nextInt(tags.size());
            Cocktail cocktail = cocktailRepository.findById(tags.get(n).getCocktail().getId());
            preferCocktail.add(new CocktailResponseDto(cocktail, userLikeCocktailRepository.findByCocktailIdAndUserId(cocktail.getId(), userRepository.findByUsername(authentication.getName()).getId())));
            tags.remove(n);
        }
        return preferCocktail;
    }
}
