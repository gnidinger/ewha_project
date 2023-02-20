import { useState } from 'react';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import TabPanel from './TabPanel';
import Box from '@mui/material/Box';
import { myComments, myLikes, myPosts } from '../../api/user';

const a11yProps = (index: number) => {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

const Activity = () => {
  const [value, setValue] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <Box>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label='basic tabs example' variant='scrollable' scrollButtons='auto'>
          <Tab label='나의 글' {...a11yProps(0)} />
          <Tab label='나의 댓글' {...a11yProps(1)} />
          <Tab label='나의 공감' {...a11yProps(2)} />
          {/* <Tab label='나의 질문' {...a11yProps(3)} /> */}
        </Tabs>
      </Box>
      <TabPanel value={value} index={0} getFunc={myPosts} />
      <TabPanel value={value} index={1} getFunc={myComments} />
      <TabPanel value={value} index={2} getFunc={myLikes} />
      {/* <TabPanel value={value} index={3} /> */}
    </Box>
  );
};

export default Activity;