import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import useHotFeedsLoad from 'hooks/queries/feed/useHotFeedsLoad';
import RegularCard from 'components/RegularCard/RegularCard';
import ROUTE from 'constants/routes';
import Styled, { CarouselArrowButton } from './HotFeedsContent.styles';
import useSnackbar from 'contexts/snackbar/useSnackbar';

const HotFeedsContent = () => {
  const [hotToyCardIdx, setHotToyCardIdx] = useState(3);

  const snackbar = useSnackbar();

  const { data: hotFeeds } = useHotFeedsLoad({
    errorHandler: (error) => {
      snackbar.addSnackbar('error', error.message);
    },
  });

  const showPreviousCards = () => {
    if (hotToyCardIdx > 1) setHotToyCardIdx(hotToyCardIdx - 1);
  };

  const showFollowingCards = () => {
    if (hotToyCardIdx < hotFeeds?.length) setHotToyCardIdx(hotToyCardIdx + 1);
  };

  return (
    <>
      <CarouselArrowButton onClick={showPreviousCards}>
        <Styled.CarouselLeft width="20px" height="20px" />
      </CarouselArrowButton>
      <Styled.HotToyCardsContainer position={hotToyCardIdx}>
        {hotFeeds &&
          hotFeeds.map((feed, idx) => (
            <Styled.HotToyCardWrapper key={feed.id} offset={idx + 1} position={hotToyCardIdx}>
              <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
                <Styled.VerticalAvatar user={feed.author} />
                <RegularCard feed={feed} />
              </Link>
            </Styled.HotToyCardWrapper>
          ))}
      </Styled.HotToyCardsContainer>

      <CarouselArrowButton onClick={showFollowingCards}>
        <Styled.CarouselRight width="20px" height="20px" />
      </CarouselArrowButton>
    </>
  );
};

export default HotFeedsContent;
