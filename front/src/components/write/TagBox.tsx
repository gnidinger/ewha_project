import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import { interests } from '../common/interestsList';

interface Props {
  innerRef: React.MutableRefObject<undefined>,
  display: string,
  currentTags: string[],
  add: (tag: string) => void
}

const wrapperBox = {
  width: '9rem',
  position: 'absolute',
  zIndex: '1',
  backgroundColor: 'white',
  border: '0.1rem solid lightGray',
  left: '1.2rem',
  top: '1.2rem'
}

const TagBox = ({ innerRef, display, currentTags, add }: Props) => {
  const [itemList, setItemList] = useState<string[][]>([]);

  const handleClickItem = (event: React.MouseEvent<HTMLElement>) => {
    add(event.currentTarget.id);
  };

  useEffect(() => {
    const newItemList = interests.filter(interest => !(currentTags.includes(interest[1])));
    setItemList(newItemList);
  }, [currentTags]);

  return(
    <Box ref={innerRef} sx={{ ...wrapperBox, display: {display} }}>
      <List>
        {itemList.map((interest) => (
          <ListItemButton onClick={handleClickItem} key={interest[1]} id={interest[1]}>
            <ListItemText primary={interest[0]} />
          </ListItemButton>
        ))}
      </List>
    </Box>
  );
};

export default TagBox;