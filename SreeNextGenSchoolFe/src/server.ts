import {
  AngularNodeAppEngine,
  createNodeRequestHandler,
  isMainModule,
  writeResponseToNodeResponse,
} from '@angular/ssr/node';
import express from 'express';
import { join } from 'node:path';

const browserDistFolder = join(import.meta.dirname, '../browser');
const backendUrl = process.env['BACKEND_URL'] || 'http://localhost:8080';

const app = express();
const angularApp = new AngularNodeAppEngine();

app.use('/api', express.raw({ type: '*/*' }), async (req, res, next) => {
  try {
    const targetPath = req.originalUrl.replace(/^\/api/, '') || '/';
    const targetUrl = new URL(targetPath, backendUrl);
    const headers = new Headers(req.headers as Record<string, string>);

    headers.delete('host');
    headers.delete('content-length');
    headers.delete('origin');

    const response = await fetch(targetUrl, {
      method: req.method,
      headers,
      body: req.method === 'GET' || req.method === 'HEAD' ? undefined : req.body,
    });

    response.headers.forEach((value, key) => res.setHeader(key, value));
    res.status(response.status).send(Buffer.from(await response.arrayBuffer()));
  } catch (error) {
    next(error);
  }
});

/**
 * Serve static files from /browser
 */
app.use(
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: false,
    redirect: false,
  }),
);

/**
 * Handle all other requests by rendering the Angular application.
 */
app.use((req, res, next) => {
  angularApp
    .handle(req)
    .then((response) =>
      response ? writeResponseToNodeResponse(response, res) : next(),
    )
    .catch(next);
});

/**
 * Start the server if this module is the main entry point, or it is ran via PM2.
 * The server listens on the port defined by the `PORT` environment variable, or defaults to 4000.
 */
if (isMainModule(import.meta.url) || process.env['pm_id']) {
  const port = process.env['PORT'] || 4000;
  app.listen(port, (error) => {
    if (error) {
      throw error;
    }

    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

/**
 * Request handler used by the Angular CLI (for dev-server and during build) or Firebase Cloud Functions.
 */
export const reqHandler = createNodeRequestHandler(app);
