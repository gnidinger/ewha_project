import React, { useState, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Header } from '../components/common';
import { TagBox } from '../components/write';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import IconButton from '@mui/material/IconButton';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import { Button } from '@mui/material';
import { INTERESTS, interestsObject } from '../components/common/interestsList';
import { writePost } from '../api/post';

export interface PostData {
  title: string,
  categories: Object[],
  body: string,
  imagePath?: string | null
}

const Write = () => {
  const tagboxRef: any = useRef();
  const navigation = useNavigate();
  const location = useLocation();
  const feedData = location.state ? location.state.feedData : undefined;

  const [tags, setTags] = useState<string[]>(feedData ? feedData.categories : []);
  const [tagbox, setTagbox] = useState<string>('none');
  const [imageFile, setImageFile] = useState<string | null>(feedData ? feedData.imagePath : null);

  const handleSubmit = async(event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const postData: PostData = { title: '', categories: [], body: '' };
    const formData = new FormData(event.currentTarget);
    const entries = formData.entries();
    for (const pair of entries) {
      if(pair[0] === 'title') postData.title = String(pair[1]);
      else if(pair[0] === 'body') postData.body = String(pair[1]);
    }
    postData.categories = tags.map((tag) => { return { categoryType: tag } });
    if(imageFile && !formData.get('image')) {
      postData.imagePath = imageFile;
      await writePost(postData, undefined, feedData.feedId);
    }
    if((formData.get('image') as File).name) await writePost(postData, (formData.get('image') as File));
    else await writePost(postData);
    navigation('/ari');
  };

  const handleClickAddBtn = () => {
    if(tagbox === 'none') setTagbox('block');
    else setTagbox('none');
  };

  const clickOutside = (event: React.MouseEvent<HTMLElement>) => {
    if(tagboxRef.current && !tagboxRef.current.contains(event.target)) setTagbox('none');
  };

  const addTag = (tag: string): void => {
    setTags([...tags, tag]);
    setTagbox('none');
  };

  const handleDeleteChip = (event: React.MouseEvent<HTMLElement>) => {
    const newTags = [...tags];
    newTags.splice(parseInt((event.currentTarget.parentNode as HTMLElement).id), 1);
    setTags(newTags);
  };

  const reader = new FileReader();
  const addImage = (event: React.ChangeEvent<HTMLInputElement>) => {
    if(event.target.files && event.target.files[0]) {
      reader.onload = () => {
        const dataURL = reader.result;
        setImageFile(String(dataURL));
      }
      reader.readAsDataURL(event.target.files[0]);
    }
  };

  return(
    <>
      <Header>새 게시글 작성</Header>
      <Box onClick={tagbox === 'block' ? clickOutside : undefined} id='main' component='form' onSubmit={handleSubmit} sx={{ padding: 3 }}>
        <Grid item xs={12}>
          <TextField
            name='title'
            fullWidth
            id='title'
            label='제목'
            autoFocus
            defaultValue={feedData && feedData.title}
            sx={{ mb: 1 }}
          />
          <Stack direction='row' sx={{ mb: 1 }}>
            <span style={{ lineHeight: '32px', verticalAlign: 'middle', margin: '0 1rem' }}>태그</span>
            {tags.map((tag, index) => (
              <Chip key={tag} id={String(index)} label={interestsObject[tag as keyof INTERESTS]} onDelete={handleDeleteChip} sx={{ marginRight: '0.5rem' }} />
            ))}
            {tags.length < 3 &&
              <Box sx={{ position: 'relative' }}>
                <IconButton onClick={handleClickAddBtn} sx={{ padding: '0' }}>
                  <AddCircleOutlineIcon fontSize='large' />
                </IconButton>
                <TagBox innerRef={tagboxRef} display={tagbox} currentTags={tags} add={addTag} />
              </Box>
            }
          </Stack>
          {imageFile &&
          <Box sx={{ width: '100%', maxWidth: '32rem' }}>
            <img style={{ width: '100%' }} src={imageFile} />
          </Box>
          }
          <TextField
            name='body'
            fullWidth
            id='body'
            label='내용'
            multiline
            defaultValue={feedData && feedData.body}
            rows={15}
          />
        </Grid>
        <Box sx={{ mt: 2 }}>
          <input id='photo' name='image' type='file' accept='image/*' onChange={addImage} hidden />
          <label htmlFor='photo'>
            <Button component='span' variant='contained'>{imageFile ? '사진 변경' : '사진 첨부' }</Button>
          </label>
          <Button style={{ float: 'right' }} type='submit' variant='contained'>등록</Button>
        </Box>
      </Box>
    </>
  );
};

export default Write;