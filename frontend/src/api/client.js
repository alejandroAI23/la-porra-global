const API_BASE = '/api'

let authToken = localStorage.getItem('token')

export function setToken(token) {
  authToken = token
  if (token) localStorage.setItem('token', token)
  else localStorage.removeItem('token')
}

export function getToken() {
  return authToken
}

export function isAuthenticated() {
  return Boolean(authToken)
}

export function logout() {
  setToken(null)
}

async function request(path, { method = 'GET', body } = {}) {
  const headers = { 'Content-Type': 'application/json' }
  if (authToken) headers.Authorization = `Bearer ${authToken}`

  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  })

  if (res.status === 401) {
    setToken(null)
  }

  if (!res.ok) {
    const error = await res.json().catch(() => ({}))
    throw new Error(error.message || `Error ${res.status}`)
  }
  return res.status === 204 ? null : res.json()
}

export const api = {
  // auth
  register: (data) => request('/auth/register', { method: 'POST', body: data }),
  login: (data) => request('/auth/login', { method: 'POST', body: data }),

  // users
  getMe: () => request('/users/me'),
  updateMe: (data) => request('/users/me', { method: 'PATCH', body: data }),

  // tournament
  getMatches: (status) =>
    request(status ? `/matches?status=${encodeURIComponent(status)}` : '/matches'),
  getMatch: (id) => request(`/matches/${id}`),

  // leagues
  createLeague: (data) => request('/leagues', { method: 'POST', body: data }),
  joinLeague: (id, data) =>
    request(`/leagues/${id}/join`, { method: 'POST', body: data }),
  getLeague: (id) => request(`/leagues/${id}`),
  getLeagueMembers: (id) => request(`/leagues/${id}/members`),
  getLeagueRanking: (id) => request(`/leagues/${id}/ranking`),

  // predictions
  createPrediction: (data) =>
    request('/predictions', { method: 'POST', body: data }),
  getMyPredictions: () => request('/predictions/me'),

  // bars
  getBars: () => request('/bars'),
  createBar: (data) => request('/bars', { method: 'POST', body: data }),
  getBarEvents: (id) => request(`/bars/${id}/events`),
  createBarEvent: (id, data) =>
    request(`/bars/${id}/events`, { method: 'POST', body: data }),

  // notifications
  getNotifications: (unreadOnly = false) =>
    request(`/notifications${unreadOnly ? '?unreadOnly=true' : ''}`),
  getUnreadCount: () => request('/notifications/unread-count'),
  markNotificationRead: (id) =>
    request(`/notifications/${id}/read`, { method: 'PATCH' }),

  // payments
  getPlans: () => request('/payments/plans'),
  getSubscription: () => request('/payments/subscription'),
  subscribe: (data) => request('/payments/subscribe', { method: 'POST', body: data }),
  cancelSubscription: () => request('/payments/cancel', { method: 'POST' }),

  // ai
  getSuggestion: (matchId) => request(`/ai/matches/${matchId}/suggestion`),
}
