import React from 'react';

interface MotorPHLogoProps {
  className?: string;
  size?: number | string;
}

export default function MotorPHLogo({ className = '', size = 48 }: MotorPHLogoProps) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 512 512"
      width={size}
      height={size}
      className={className}
    >
      {/* Background with rounded corners */}
      <rect width="512" height="512" rx="120" fill="#6c8ef5" />

      {/* Motorcycle Icon */}
      <g
        fill="none"
        stroke="#ffffff"
        strokeWidth="24"
        strokeLinecap="round"
        strokeLinejoin="round"
      >
        {/* Front Wheel */}
        <circle cx="380" cy="340" r="60" />
        {/* Rear Wheel */}
        <circle cx="132" cy="340" r="60" />

        {/* Body / Frame & Handlebars */}
        <path d="M132 340 L210 240 L310 240 L360 300" />
        <path d="M260 240 L240 160 L280 150" />
        <path d="M340 260 L390 220" />

        {/* Engine / Core Details */}
        <path d="M190 340 L230 280 L290 340" />
      </g>
    </svg>
  );
}
