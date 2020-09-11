> ExpressJS HTTP server that acts as a facade to the TERN DOI minting service.

# Developer quickstart
  1. clone repo
  1. install dependencies
      ```bash
      yarn
      ```
  1. run the server
      ```bash
      yarn start:watch
      # alternatively you can override which DOI server you act as a facade for
      DOI_URL=https://doi.tern.uq.edu.au/test/ yarn start:watch
      ```

# Why does this exist?
The DOI minting service endpoint is HTTPS, as it should be, but this app has
trouble calling endpoints as managing trusted certificates is a chore. There's
also the challenge of forming the XML request, which in Java-land means using
JAXB bindings. None of this is fun. So this service provides a non-secure HTTP
endpoint within the Docker stack and means we can pass the minimum of data from
Java and doing most of the heavy lifting for XML transformation in JS.

# How to deploy
Launch with:
```bash
yarn start:prod
```
