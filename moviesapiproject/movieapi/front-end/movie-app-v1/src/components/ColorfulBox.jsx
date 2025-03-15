import React, { useState, useEffect } from "react";

const ColorfulBox = () => {
  const [color, setColor] = useState("#ff5733");

  useEffect(() => {
    const changeColor = () => {
      const randomColor = `#${Math.floor(Math.random() * 16777215).toString(16)}`;
      setColor(randomColor);
    };
    const interval = setInterval(changeColor, 1000); // Change color every second
    return () => clearInterval(interval);
  }, []);

  return (
    <div
      className="w-64 h-64 flex flex-col items-center justify-center text-white text-xl font-bold rounded-lg shadow-lg transition-all duration-500"
      style={{
        background: `linear-gradient(135deg, ${color}, #000000)`,
      }}
    >
      <img
        src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Anatomy_of_a_Sunset-2.jpg/1200px-Anatomy_of_a_Sunset-2.jpg" // Replace with your image URL
        alt="Sample"
        className="w-24 h-24 rounded-full mb-2"
      />
      Colorful Box
    </div>
  );
};

export default ColorfulBox;
