import { Link } from 'react-router-dom';
import Card from '@mui/material/Card';
import Typography from '@mui/material/Typography';
import FavoriteIcon from '@mui/icons-material/Favorite';
import MessageOutlinedIcon from '@mui/icons-material/MessageOutlined';
import { pink } from '@mui/material/colors';
import { ItemProps } from './TabPanel';

const Item = ({ feedId, title, body, likeCount, commentCount, isComment }: ItemProps) => {
  return(
    <Link to={`/post/${feedId}`}>
      <Card variant='outlined' sx={{ padding: '0.7rem' }}>
        <Typography sx={{ fontSize: 15 }} gutterBottom>
          {title}
        </Typography>
        <Typography sx={{ fontSize: 12 }} gutterBottom>
          {body.length > 50 ? body.substring(0, 49) + '...' : body}
        </Typography>
        <Typography sx={{ fontSize: 11, textAlign: 'right' }}>
          <FavoriteIcon sx={{ fontSize: 14, color: pink[500], verticalAlign: 'middle' }} />
          {` ${likeCount ? likeCount : 0} `}
          {!isComment &&
          <span>
            <MessageOutlinedIcon sx={{ fontSize: 14, verticalAlign: 'middle' }} />
            {` ${commentCount ? commentCount : 0} `}
          </span>
          }
        </Typography>
      </Card>
    </Link>
  );
};

export default Item;