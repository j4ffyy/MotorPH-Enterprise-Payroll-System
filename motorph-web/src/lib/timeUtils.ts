/**
 * Time Utility for Philippine Standard Time (PST, UTC+8)
 * Ensures consistent time synchronization across cloud/serverless production environments.
 */

export function getPHNow(): Date {
  const options: Intl.DateTimeFormatOptions = {
    timeZone: 'Asia/Manila',
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false,
  };
  const formatter = new Intl.DateTimeFormat('en-US', options);
  const parts = formatter.formatToParts(new Date());

  const partMap: Record<string, string> = {};
  for (const part of parts) {
    if (part.type !== 'literal') {
      partMap[part.type] = part.value;
    }
  }

  const year = Number(partMap.year);
  const month = Number(partMap.month) - 1;
  const day = Number(partMap.day);
  const hour = Number(partMap.hour) === 24 ? 0 : Number(partMap.hour);
  const minute = Number(partMap.minute);
  const second = Number(partMap.second);

  return new Date(year, month, day, hour, minute, second);
}

export function getPHDateString(): string {
  const phDate = getPHNow();
  const y = phDate.getFullYear();
  const m = String(phDate.getMonth() + 1).padStart(2, '0');
  const d = String(phDate.getDate()).padStart(2, '0');
  return `${y}-${m}-${d}`;
}

export function getPHTTimeString(): string {
  const phDate = getPHNow();
  const h = String(phDate.getHours()).padStart(2, '0');
  const min = String(phDate.getMinutes()).padStart(2, '0');
  const s = String(phDate.getSeconds()).padStart(2, '0');
  return `${h}:${min}:${s}`;
}

/**
 * Formats a date string (e.g. YYYY-MM-DD) into human-readable text (e.g. "October 16, 2026").
 */
export function formatHumanDate(dateStr: string): string {
  if (!dateStr) return '—';
  const parts = dateStr.split('-');
  if (parts.length === 3) {
    const [year, month, day] = parts.map(Number);
    const dateObj = new Date(year, month - 1, day);
    if (!isNaN(dateObj.getTime())) {
      return dateObj.toLocaleDateString('en-US', {
        month: 'long',
        day: 'numeric',
        year: 'numeric',
      });
    }
  }
  const d = new Date(dateStr);
  if (!isNaN(d.getTime())) {
    return d.toLocaleDateString('en-US', {
      month: 'long',
      day: 'numeric',
      year: 'numeric',
    });
  }
  return dateStr;
}
