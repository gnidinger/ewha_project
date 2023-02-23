import { useState } from 'react';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderRoundedIcon from '@mui/icons-material/FavoriteBorderRounded';
import { CommentData } from '../../pages/View';
import { heartIconStyle } from './Content';
import { like, dislike, modify, deleteComment } from '../../api/comment';

interface Props {
  comment: CommentData, 
  rerender: () => void,
  setShowInput: React.Dispatch<React.SetStateAction<boolean>>
}

const Comment = ({ comment, rerender, setShowInput }: Props) => {
  const [editMode, setEditMode] = useState<boolean>(false);

  const clickLike = async() => {
    await like(comment.commentId);
    rerender();
  };

  const clickDislike = async() => {
    await dislike(comment.commentId);
    rerender();
  };

  const clickEditBtn = () => {
    setEditMode(true);
    setShowInput(false);
  };

  const handleEdit = async() => {
    await modify(comment.commentId, (document.getElementsByName('edit')[0] as HTMLFormElement).value);
    setEditMode(false);
    setShowInput(true);
  };

  const handleDelete = async() => {
    await deleteComment(comment.commentId);
    rerender();
  };

  return(
    <Box sx={{ mb: 2 }}>
      <Avatar src={comment.userInfo.profileImage} sx={{ float: 'left', width: 32, height: 32 }} />
      <Typography sx={{ lineHeight: '32px', verticalAlign: 'middle', ml: 5 }} gutterBottom>{comment.userInfo.nickname}</Typography>
      {editMode ?
        <Box component='form' onSubmit={handleEdit} sx={{ display: 'grid', gridTemplateColumns: 'auto 5rem' }}>
          <TextField
            fullWidth
            name='edit'
            defaultValue={comment.body}
          />
          <Button type='submit' variant='contained' sx={{ ml: 1 }}>수정</Button>
        </Box> :
        <>
        {comment.isMyComment && <Button onClick={clickEditBtn} sx={{ float: 'right' }}>수정하기</Button>}
        {comment.isMyComment && <Button onClick={handleDelete} sx={{ float: 'right' }}>삭제하기</Button>}
        <Typography>{comment.body}</Typography>
        </>
      }
      {comment.isLikedComment ?
      <FavoriteIcon onClick={clickDislike} sx={heartIconStyle} /> :
      <FavoriteBorderRoundedIcon onClick={clickLike} sx={heartIconStyle} />
      } {comment.likeCount}
    </Box>
  );
};

export default Comment;