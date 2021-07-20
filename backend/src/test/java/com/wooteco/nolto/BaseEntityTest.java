package com.wooteco.nolto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BaseEntityTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    private Feed feed1;
    private Feed feed2;
    private Feed feed3;

    @BeforeEach
    void setUp() {
        user1 = new User("123456L", "github", "아마찌", "imageUrl");
        user2 = new User("654321L", "github", "지그", "imageUrl");

        userRepository.save(user1);
        userRepository.save(user2);

        feed1 = new Feed("title1", "content1", Step.PROGRESS, true,
                "storageUrl", "", "http://thumbnailUrl.ppnngg");
        feed2 = new Feed("title2", "content2", Step.PROGRESS, false,
                "", "deployUrl", "http://thumbnailUrl.pnggg");
        feed3 = new Feed("title3", "content3", Step.COMPLETE, false,
                "storageUrl", "deployUrl", "http://thumbnailUrl.ddd");

        feed1.writtenBy(user1);
        feed2.writtenBy(user2);
        feed3.writtenBy(user2);
    }

    @DisplayName("피드 저장 시 생성 날짜가 저장된다.")
    @Test
    void save() {
        // when
        Feed savedFeed1 = feedRepository.save(feed1);
        Feed savedFeed2 = feedRepository.save(feed2);
        Feed savedFeed3 = feedRepository.save(feed3);
        entityManager.flush();

        // then
        assertThat(savedFeed1.getCreatedDate()).isNotNull();
        assertThat(savedFeed1.getModifiedDate()).isNotNull();
    }

    @DisplayName("데이터 변경 시 lastModifiedDate가 수정된다")
    @Test
    void update() {
        // given
        Feed savedFeed2 = feedRepository.save(feed2);
        LocalDateTime modifiedDate = savedFeed2.getModifiedDate(); // 생성 시점

        // when
        savedFeed2.writtenBy(user1);
        entityManager.flush();
        entityManager.clear();

        // then
        Feed findFeed = feedRepository.findById(savedFeed2.getId()).get();
        LocalDateTime changedUpdatedAt = findFeed.getModifiedDate();
        assertThat(modifiedDate).isNotEqualTo(changedUpdatedAt);
    }

}