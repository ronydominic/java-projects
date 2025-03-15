import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';  // Basic Swiper styles
import 'swiper/css/navigation';  // Navigation buttons
import 'swiper/css/pagination';  // Pagination dots
import { Paper } from '@mui/material';  // For card-like styling
import './Hero.css'

import { Navigation, Pagination, Autoplay } from 'swiper/modules';

const Hero = ({ movies }) => {
  return (
    <div className="w-full max-w-3xl mx-auto mt-10">
      <Swiper
        modules={[Navigation, Pagination, Autoplay]}
        spaceBetween={30}
        slidesPerView={1}
        navigation
        pagination={{ clickable: true }}
        autoplay={{ delay: 3000 }}
        loop={true}
      >
        {
          movies.map((movie) => (
            <SwiperSlide key={movie.id}>  {/* Always use a unique key */}
              <Paper elevation={3} className='p-4 rounded-lg'>
                <div className='movie-card-container'>
                  <div className='movie-card'>
                    <div className='movie-detail'>
                      <div className='movie-poster'>
                        <img src={movie.poster} alt={movie.title} className='w-full h-64 object-cover rounded-lg' />
                      </div>
                      <div className='movie-title mt-4 text-center'>
                        <h4 className='text-xl font-semibold'>{movie.title}</h4>
                      </div>
                    </div>
                  </div>
                </div>
              </Paper>
            </SwiperSlide>
          ))
        }
      </Swiper>
    </div>
  );
}

export default Hero;
