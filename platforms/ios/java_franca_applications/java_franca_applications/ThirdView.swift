//
//  ThirdView.swift
//  java_franca_applications
//
//  Created by admin on 12.06.2026.
//

import SwiftUI

struct ThirdView: View {
  var body: some View {
    ZStack {
      Color.black.ignoresSafeArea()
      Text("Третья вьюшка")
        .font(.largeTitle)
        .foregroundColor(.white)
    }
  }
}

#Preview {
  ThirdView()
}
