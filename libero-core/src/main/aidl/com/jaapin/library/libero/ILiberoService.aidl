// ILiberoService.aidl
package com.jaapin.library.libero;

import com.jaapin.library.libero.model.Request;
import com.jaapin.library.libero.model.Response;

interface ILiberoService {

    Response send(in Request request);
}
