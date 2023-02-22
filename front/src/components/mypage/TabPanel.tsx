import { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Item from './Item';

interface TabPanelProps {
  index: number,
  value: number,
  getFunc: () => Promise<any>
}

export interface ItemProps {
  feedId: number,
  title: string,
  body: string,
  likeCount: number, 
  commentCount: number
}

const TabPanel = ({ value, index, getFunc }: TabPanelProps) => {
  const [itemList, setItemList] = useState<ItemProps[]>([]);

  const getItemList = async() => {
    const data = await getFunc();
    setItemList(data);
  };

  useEffect(() => {
    getItemList();
  }, [])

  return (
    <div
      role='tabpanel'
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
    >
      {value === index && itemList.map((item, idx) => (
        <Box key={idx} sx={{ paddingTop: '1rem' }}>
          <Item
            feedId={item.feedId}
            title={item.title}
            body={item.body}
            likeCount={item.likeCount}
            commentCount={item.commentCount} />
        </Box>)
      )}
    </div>
  );
};

export default TabPanel;