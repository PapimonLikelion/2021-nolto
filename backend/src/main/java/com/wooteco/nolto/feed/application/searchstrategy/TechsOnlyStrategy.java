package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class TechsOnlyStrategy extends SearchStrategy {

    public TechsOnlyStrategy(FeedRepository feedRepository) {
        super(feedRepository);
    }

    @Override
    public List<Feed> search(String query, String techs) {
        return searchByTechs(techs);
    }

    @Override
    public List<Feed> searchWithCondition(String query, String techs, boolean help, long nextFeedId, EnumSet<Step> steps, Pageable pageable) {
        List<String> techNames = Arrays.asList(techs.split(TECH_SEARCH_DELIMITER));
        Set<Boolean> helpCondition;
        if (help) {
            helpCondition = new HashSet<>(Collections.singletonList(true));
            return feedRepository.findByTechs(techNames, helpCondition, nextFeedId, steps, pageable);
        }
        helpCondition = new HashSet<>(Arrays.asList(true, false));
        return feedRepository.findByTechs(techNames, helpCondition, nextFeedId, steps, pageable);
    }
}
